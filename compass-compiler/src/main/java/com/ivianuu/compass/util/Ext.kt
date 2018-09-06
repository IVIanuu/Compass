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
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror


fun Element.shouldBeSerialized() =
    !MoreElements.isAnnotationPresent(this, DoNotSerialize::class.java)

fun Element.getCompassConstructor(): ExecutableElement {
    return ConstructorSelector.getCompassConstructor(this)
}

fun TypeElement.serializerClassName() = className("__Serializer")

fun TypeElement.routeFactoryClassName() = className("__RouteFactory")

fun TypeElement.routeProviderClassName() = className("__RouteProvider")

fun TypeElement.serializerProviderClassName() = className("__SerializerProvider")

fun TypeElement.detourProviderClassName() = className("__DetourProvider")

private fun TypeElement.className(suffix: String) =
    ClassName(packageName(), baseClassName() + suffix)

private fun TypeElement.baseClassName(): String {
    val packageName = packageName()
    return qualifiedName.toString().substring(
        packageName.length + 1
    ).replace('.', '_')
}

fun Element.packageName() =
    MoreElements.getPackage(enclosingElement).qualifiedName.toString()

val Element.isKotlinObject: Boolean get() {
    return if (this is TypeElement) {
        enclosedElements
            .any { it.simpleName.toString() == "INSTANCE" }
    } else {
        false
    }
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
        when {
            processingEnv.typeUtils.isAssignable(
                this.asType(),
                processingEnv.elementUtils.getTypeElement(CLASS_ACTIVITY.toString()).asType()
            ) -> TargetType.ACTIVITY
            processingEnv.typeUtils.isAssignable(
                this.asType(),
                processingEnv.elementUtils.getTypeElement(CLASS_FRAGMENT.toString()).asType()
            ) -> TargetType.FRAGMENT
            else -> TargetType.UNKNOWN
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