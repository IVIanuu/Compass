/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.compass.util

import com.google.auto.common.MoreElements
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour
import com.ivianuu.compass.DoNotSerialize
import com.ivianuu.compass.RouteFactory
import com.ivianuu.compass.serializer.ConstructorSelector
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

fun Element.shouldBeSerialized() =
    !MoreElements.isAnnotationPresent(this, DoNotSerialize::class.java)

fun Element.getCompassConstructor(): ExecutableElement {
    return ConstructorSelector.getCompassConstructor(this)
}

fun Element.serializerClassName(): ClassName {
    return ClassName.bestGuess("${this.simpleName}__Serializer")
}

fun Element.routeFactoryClassName(): ClassName {
    return ClassName.bestGuess("${this.simpleName}__RouteFactory")
}

fun Element.routeProviderClassName(): ClassName {
    return ClassName.bestGuess("${this.simpleName}__RouteProvider")
}

fun Element.serializerProviderClassName(): ClassName {
    return ClassName.bestGuess("${this.simpleName}__SerializerProvider")
}

fun Element.detourProviderClassName(): ClassName {
    return ClassName.bestGuess("${this.simpleName}__DetourProvider")
}

fun Element.packageName(environment: ProcessingEnvironment): String {
    return environment.elementUtils.getPackageOf(this).toString()
}

val Element.isKotlinObject: Boolean get() {
    return if (this is TypeElement) {
        enclosedElements
            .any { it.simpleName.toString() == "INSTANCE" }
    } else {
        false
    }
}

fun isSubtypeOfType(typeMirror: TypeMirror, otherType: String): Boolean {
    if (isTypeEqual(typeMirror, otherType)) {
        return true
    }
    if (typeMirror.kind != TypeKind.DECLARED) {
        return false
    }
    val declaredType = typeMirror as DeclaredType
    val typeArguments = declaredType.typeArguments
    if (typeArguments.size > 0) {
        val typeString = StringBuilder(declaredType.asElement().toString())
        typeString.append('<')
        for (i in typeArguments.indices) {
            if (i > 0) {
                typeString.append(',')
            }
            typeString.append('?')
        }
        typeString.append('>')
        if (typeString.toString() == otherType) {
            return true
        }
    }
    val element = declaredType.asElement() as? TypeElement ?: return false
    val superType = element.superclass
    if (isSubtypeOfType(superType, otherType)) {
        return true
    }
    for (interfaceType in element.interfaces) {
        if (isSubtypeOfType(interfaceType, otherType)) {
            return true
        }
    }
    return false
}

fun isTypeEqual(typeMirror: TypeMirror, otherType: String): Boolean {
    return otherType == typeMirror.toString()
}

val Element.destinationTarget: TypeMirror?
    get() {
        try {
            this.getAnnotation(Destination::class.java).target
        } catch (e: MirroredTypesException) {
            return e.typeMirrors.firstOrNull()
        } catch (e: Exception) {
            return null
        }

        return null
    }

val Element.routeFactoryClass: TypeMirror?
    get() {
        try {
            this.getAnnotation(RouteFactory::class.java).clazz
        } catch (e: MirroredTypesException) {
            return e.typeMirrors.firstOrNull()
        } catch (e: Exception) {
            return null
        }

        return null
    }

val Element.detourClass: TypeMirror?
    get() {
        try {
            this.getAnnotation(Detour::class.java).clazz
        } catch (e: MirroredTypesException) {
            return e.typeMirrors.firstOrNull()
        } catch (e: Exception) {
            return null
        }

        return null
    }

fun Element?.targetType(processingEnv: ProcessingEnvironment): TargetType {
    return if (this != null) {
        try {
            when {
                processingEnv.typeUtils.isAssignable(
                    this.asType(),
                    processingEnv.elementUtils.getTypeElement(CLASS_ACTIVITY.toString()).asType()
                ) -> TargetType.ACTIVITY
                processingEnv.typeUtils.isAssignable(
                    this.asType(),
                    processingEnv.elementUtils.getTypeElement(CLASS_FRAGMENT.toString()).asType()
                ) -> TargetType.FRAGMENT
                processingEnv.typeUtils.isAssignable(
                    this.asType(),
                    processingEnv.elementUtils.getTypeElement(CLASS_FRAGMENT_X.toString()).asType()
                ) -> TargetType.FRAGMENTX
                else -> TargetType.UNKNOWN
            }
        } catch (e: Exception) {
            TargetType.UNKNOWN
        }
    } else {
        TargetType.UNKNOWN
    }
}

fun FileSpec.write(processingEnv: ProcessingEnvironment) {
    val path = processingEnv.options["kapt.kotlin.generated"]
        ?.replace("kaptKotlin", "kapt")!!
    writeTo(File(path))
}