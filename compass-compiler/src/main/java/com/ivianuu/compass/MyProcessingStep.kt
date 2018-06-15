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

package com.ivianuu.compass

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.route.RouteFactoryBuilder
import com.ivianuu.compass.route.RouteProviderBuilder
import com.ivianuu.compass.util.packageName
import com.ivianuu.compass.util.routeFactoryClassName
import com.ivianuu.compass.util.routeProviderClassName
import com.ivianuu.compass.util.write
import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * @author Manuel Wrage (IVIanuu)
 */
class MyProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .filterIsInstance<TypeElement>()
            .onEach { element -> generateRouteFactory(element) }
            .onEach { element -> generateRouteProvider(element) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Destination::class.java)

    private fun generateRouteFactory(base: TypeElement) {
        val type = RouteFactoryBuilder.buildRouteFactory(processingEnv, base)

        if (type != null) {
            val packageName = base.packageName(processingEnv)
            val className = base.routeFactoryClassName()

            val file = FileSpec.builder(packageName, className.toString())
                .addType(type)
                .build()

            file.write(processingEnv)
        }
    }

    private fun generateRouteProvider(base: TypeElement) {
        val type = RouteProviderBuilder.buildRouteProvider(processingEnv, base)

        val packageName = base.packageName(processingEnv)
        val className = base.routeProviderClassName()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.write(processingEnv)
    }
}