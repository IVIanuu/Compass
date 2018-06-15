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
import com.ivianuu.compass.detour.DetourProviderBuilder
import com.ivianuu.compass.extension.ExtensionBuilder
import com.ivianuu.compass.route.RouteFactoryBuilder
import com.ivianuu.compass.route.RouteProviderBuilder
import com.ivianuu.compass.serializer.SerializerBuilder
import com.ivianuu.compass.serializer.SerializerProviderBuilder
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * @author Manuel Wrage (IVIanuu)
 */
class ProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .filterIsInstance<TypeElement>()
            .onEach { element -> generateSerializer(element) }
            .onEach { element -> generateSerializerProvider(element) }
            .onEach { element -> generateRouteFactory(element) }
            .onEach { element -> generateRouteProvider(element) }
            .onEach { element -> generateDetourProvider(element) }
            .onEach { element -> generateExtensions(element) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Destination::class.java)

    private fun generateSerializer(base: TypeElement) {
        val packageName = base.serializerPackageName(processingEnv)
        val className = base.serializerClassName()

        val type = TypeSpec.objectBuilder(className)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_SERIALIZER,
                    base.asType().asTypeName()
                )
            )
            .let { SerializerBuilder.addToBundleMethod(processingEnv, it, base) }
            .let { SerializerBuilder.addFromBundleMethod(processingEnv, it, base) }
            .build()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.writeTo(File(path()))
    }

    private fun generateSerializerProvider(base: TypeElement) {
        val type = SerializerProviderBuilder.buildSerializerProvider(processingEnv, base)

        val packageName = base.serializerPackageName(processingEnv)
        val className = base.serializerProviderClassName()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.writeTo(File(path()))
    }

    private fun generateRouteFactory(base: TypeElement) {
        val type = RouteFactoryBuilder.buildRouteFactory(processingEnv, base)

        if (type != null) {
            val packageName = base.serializerPackageName(processingEnv)
            val className = base.routeFactoryClassName()

            val file = FileSpec.builder(packageName, className.toString())
                .addType(type)
                .build()

            file.writeTo(File(path()))
        }
    }

    private fun generateRouteProvider(base: TypeElement) {
        val type = RouteProviderBuilder.buildRouteProvider(processingEnv, base)

        val packageName = base.serializerPackageName(processingEnv)
        val className = base.routeProviderClassName()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.writeTo(File(path()))
    }

    private fun generateDetourProvider(base: TypeElement) {
        val type = DetourProviderBuilder.buildDetourProvider(processingEnv, base)

        if (type != null) {
            val packageName = base.serializerPackageName(processingEnv)
            val className = base.detourProviderClassName()

            val file = FileSpec.builder(packageName, className.toString())
                .addType(type)
                .build()

            file.writeTo(File(path()))
        }
    }

    private fun generateExtensions(base: TypeElement) {
        val packageName = processingEnv.elementUtils.getPackageOf(base).toString()
        val fileName = "${base.simpleName}Ext"
        val fileSpec = FileSpec.builder(packageName, fileName)

        ExtensionBuilder
            .buildSerializerFunctions(processingEnv, fileSpec, base)

        fileSpec.build().writeTo(File(path()))
    }

    private fun path(): String {
        return processingEnv.options["kapt.kotlin.generated"]
            ?.replace("kaptKotlin", "kapt")!!
    }
}