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

package com.ivianuu.compass.route

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.common.MoreElements
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.RouteFactory
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class RouteFactoryProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .filterIsInstance<TypeElement>()
            .mapNotNull(this::createDescriptor)
            .map(::RouteFactoryGenerator)
            .map(RouteFactoryGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): RouteFactoryDescriptor? {
        // the user provided his own route factory so return
        if (MoreElements.isAnnotationPresent(element, RouteFactory::class.java)) {
            return null
        }

        val target = element.destinationTarget?.let {
            processingEnv.elementUtils.getTypeElement(it.toString())
        }

        if (target == null || target.asType().toString() == "java.lang.Void") {
            /*processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "either a valid target or route factory must be specified", element)*/
            return null
        }

        val targetType = target.targetType(processingEnv)

        if (targetType == TargetType.UNKNOWN) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "unsupported target type for now only fragments and activities are supported",
                element
            )
            return null
        }

        return RouteFactoryDescriptor(
            element,
            element.packageName(processingEnv),
            element.routeFactoryClassName(),
            element.asClassName(),
            target.asClassName(),
            targetType
        )
    }
}