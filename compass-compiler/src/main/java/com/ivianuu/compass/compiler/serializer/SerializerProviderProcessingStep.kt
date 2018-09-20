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

package com.ivianuu.compass.compiler.serializer

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.common.MoreElements
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Serialize
import com.ivianuu.compass.Serializer
import com.ivianuu.compass.compiler.util.packageName
import com.ivianuu.compass.compiler.util.serializerClass
import com.ivianuu.compass.compiler.util.serializerClassName
import com.ivianuu.compass.compiler.util.serializerProviderClassName
import com.ivianuu.compass.compiler.util.shouldBeSerialized
import com.ivianuu.compass.compiler.util.write
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class SerializerProviderProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        val elements = mutableSetOf<Element>()
        elements.addAll(elementsByAnnotation[Destination::class.java])
        elements.addAll(elementsByAnnotation[Serialize::class.java])

        elements
            .asSequence()
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .mapNotNull(this::createDescriptor)
            .map(::SerializerProviderGenerator)
            .map(SerializerProviderGenerator::generate)
            .toList()
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java, Serialize::class.java)

    private fun createDescriptor(element: TypeElement): SerializerProviderDescriptor? {
        val serializerClassName =
            if (MoreElements.isAnnotationPresent(element, Serializer::class.java)) {
                if (!MoreElements.isAnnotationPresent(element, Destination::class.java)) {
                    processingEnv.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "you cannot annotate a non @destination class with @serializer", element
                    )
                    return null
                }

                val serializerClass =
                    element.serializerClass ?: return null // todo should this be a error?

                // todo check provided class

                serializerClass.asTypeName() as ClassName
            } else {
                element.serializerClassName()
            }

        return SerializerProviderDescriptor(
            element,
            element.packageName(),
            element.serializerProviderClassName(),
            serializerClassName
        )
    }
}