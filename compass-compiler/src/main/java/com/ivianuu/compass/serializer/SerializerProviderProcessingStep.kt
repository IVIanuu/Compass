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

package com.ivianuu.compass.serializer

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Serialize
import com.ivianuu.compass.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class SerializerProviderProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        val elements = mutableSetOf<Element>()
        elements.addAll(elementsByAnnotation[Destination::class.java])
        elements.addAll(elementsByAnnotation[Serialize::class.java])

        elements
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .map(this::createDescriptor)
            .map(::SerializerProviderGenerator)
            .map(SerializerProviderGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java, Serialize::class.java)

    private fun createDescriptor(element: TypeElement): SerializerProviderDescriptor {
        return SerializerProviderDescriptor(
            element,
            element.packageName(processingEnv),
            element.serializerProviderClassName(),
            element.serializerClassName()
        )
    }
}