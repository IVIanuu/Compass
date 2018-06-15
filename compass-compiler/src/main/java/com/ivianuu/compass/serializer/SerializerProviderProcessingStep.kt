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
import com.ivianuu.compass.util.packageName
import com.ivianuu.compass.util.serializerClassName
import com.ivianuu.compass.util.serializerProviderClassName
import com.ivianuu.compass.util.write
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * @author Manuel Wrage (IVIanuu)
 */
class SerializerProviderProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .filterIsInstance<TypeElement>()
            .map(this::createDescriptor)
            .map(::SerializerProviderGenerator)
            .map(SerializerProviderGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): SerializerProviderDescriptor {
        return SerializerProviderDescriptor(
            element,
            element.packageName(processingEnv),
            element.serializerProviderClassName(),
            element.serializerClassName()
        )
    }
}