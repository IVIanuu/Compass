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

package com.ivianuu.compass.compiler.util

import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour
import com.ivianuu.compass.DoNotSerialize
import com.ivianuu.compass.RouteFactory
import com.ivianuu.compass.Serializer
import com.ivianuu.compass.compiler.serializer.ConstructorSelector
import com.ivianuu.processingx.getAnnotationMirrorOrNull
import com.ivianuu.processingx.getAsTypeOrNull
import com.ivianuu.processingx.getPackage
import com.ivianuu.processingx.hasAnnotation
import com.squareup.kotlinpoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

fun Element.shouldBeSerialized() = !hasAnnotation<DoNotSerialize>()

fun Element.getCompassConstructor() =
    ConstructorSelector.getCompassConstructor(this)

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
    enclosingElement.getPackage().qualifiedName.toString()

val Element.destinationTarget
    get() = getAnnotationMirrorOrNull<Destination>()
        ?.getAsTypeOrNull("target")

val Element.routeFactoryClass
    get() = getAnnotationMirrorOrNull<RouteFactory>()
        ?.getAsTypeOrNull("clazz")

val Element.detourClass
    get() = getAnnotationMirrorOrNull<Detour>()
        ?.getAsTypeOrNull("clazz")

val Element.serializerClass
    get() = getAnnotationMirrorOrNull<Serializer>()
        ?.getAsTypeOrNull("clazz")

fun Element?.targetType(processingEnv: ProcessingEnvironment): TargetType {
    return try {
        if (this != null) {
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
    } catch (e: Exception) {
        TargetType.UNKNOWN
    }
}

val TypeElement.hasEmptyConstructor
    get() = enclosedElements
        .asSequence()
        .filterIsInstance<ExecutableElement>()
        .filter { it.kind == ElementKind.CONSTRUCTOR }
        .filter { it.modifiers.contains(Modifier.PUBLIC) }
        .any { it.parameters.isEmpty() }