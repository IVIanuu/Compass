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

import com.google.auto.service.AutoService
import com.ivianuu.compass.detour.DetourProviderBuilder
import com.ivianuu.compass.extension.ExtensionBuilder
import com.ivianuu.compass.route.RouteFactoryBuilder
import com.ivianuu.compass.route.RouteProviderBuilder
import com.ivianuu.compass.serializer.SerializerBuilder
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class CompassProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() =
        mutableSetOf(Destination::class.java.name)

    override fun getSupportedSourceVersion() = SourceVersion.latest()!!

    override fun process(set: MutableSet<out TypeElement>, env: RoundEnvironment): Boolean {
        val destinations =
            env.getElementsAnnotatedWith(Destination::class.java)
        if (destinations.isEmpty()) return false

        destinations
            .filterIsInstance<TypeElement>()
            .onEach { element -> generateSerializer(element) }
            .onEach { element -> generateRouteFactory(element) }
            .onEach { element -> generateRouteProvider(element) }
            .onEach { element -> generateDetourProvider(element) }
            .onEach { element -> generateExtensions(element) }

        return true
    }

    private fun generateSerializer(base: TypeElement) {
        val packageName = base.serializerPackageName(processingEnv)
        val className = base.serializerClassName()

        val fileUri = processingEnv.filer.createSourceFile(className.toString(), base).toUri()

        val type = TypeSpec.objectBuilder(className)
            .let { SerializerBuilder.addToBundleMethod(processingEnv, it, base) }
            .let { SerializerBuilder.addFromBundleMethod(processingEnv, it, base) }
            .build()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.writeTo(File(fileUri))
    }

    private fun generateRouteFactory(base: TypeElement) {
        val type = RouteFactoryBuilder.buildRouteFactory(processingEnv, base)

        if (type != null) {
            val packageName = base.serializerPackageName(processingEnv)
            val className = base.routeFactoryClassName()

            val fileUri = processingEnv.filer.createSourceFile(className.toString(), base).toUri()

            val file = FileSpec.builder(packageName, className.toString())
                .addType(type)
                .build()

            file.writeTo(File(fileUri))
        }
    }

    private fun generateRouteProvider(base: TypeElement) {
        val type = RouteProviderBuilder.buildRouteProvider(processingEnv, base)

        val packageName = base.serializerPackageName(processingEnv)
        val className = base.routeProviderClassName()

        val fileUri = processingEnv.filer.createSourceFile(className.toString(), base).toUri()

        val file = FileSpec.builder(packageName, className.toString())
            .addType(type)
            .build()

        file.writeTo(File(fileUri))
    }

    private fun generateDetourProvider(base: TypeElement) {
        val type = DetourProviderBuilder.buildDetourProvider(processingEnv, base)

        if (type != null) {
            val packageName = base.serializerPackageName(processingEnv)
            val className = base.detourProviderClassName()

            val fileUri = processingEnv.filer.createSourceFile(className.toString(), base).toUri()

            val file = FileSpec.builder(packageName, className.toString())
                .addType(type)
                .build()

            file.writeTo(File(fileUri))
        }
    }

    private fun generateExtensions(base: TypeElement) {
        val packageName = processingEnv.elementUtils.getPackageOf(base).toString()
        val fileName = "${base.qualifiedName}Ext"
        val fileUri = processingEnv.filer.createSourceFile(fileName, base).toUri()
        val fileSpec = FileSpec.builder(packageName, fileName)

        ExtensionBuilder
            .buildSerializerFunctions(processingEnv, fileSpec, base)

        fileSpec.build().writeTo(File(fileUri))
    }

}