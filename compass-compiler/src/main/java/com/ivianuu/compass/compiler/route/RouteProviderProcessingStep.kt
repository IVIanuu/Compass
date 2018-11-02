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

package com.ivianuu.compass.compiler.route

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.compiler.util.destinationTarget
import com.ivianuu.compass.compiler.util.isKotlinObject
import com.ivianuu.compass.compiler.util.packageName
import com.ivianuu.compass.compiler.util.routeFactoryClass
import com.ivianuu.compass.compiler.util.routeFactoryClassName
import com.ivianuu.compass.compiler.util.routeProviderClassName
import com.ivianuu.processingx.write
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class RouteProviderProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .asSequence()
            .filterIsInstance<TypeElement>()
            .mapNotNull(this::createDescriptor)
            .map(::RouteProviderGenerator)
            .map(RouteProviderGenerator::generate)
            .toList()
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): RouteProviderDescriptor? {
        val target = element.destinationTarget

        val routeFactory = element.routeFactoryClass

        val isKotlinObject = if (routeFactory != null) {
            val type = processingEnv.elementUtils.getTypeElement(routeFactory.toString())
            type.isKotlinObject
        } else {
            true
        }

        if (routeFactory != null && target != null && target.toString() != "java.lang.Void") {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "you cannot specify a target AND a route factory", element
            )
            return null
        }

        val factoryName = when {
            routeFactory != null -> routeFactory.asTypeName() as ClassName
            target != null && target.toString() != "java.lang.Void" -> element.routeFactoryClassName()
            else -> null
        }

        if (factoryName == null) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "either a valid target or route factory must be specified", element
            )
            return null
        }

        return RouteProviderDescriptor(
            element,
            element.packageName(),
            element.routeProviderClassName(),
            factoryName,
            isKotlinObject
        )
    }
}