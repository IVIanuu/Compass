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
import com.google.common.base.CaseFormat
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author Manuel Wrage (IVIanuu)
 */
class SerializerProcessingStep(
    private val processingEnv: ProcessingEnvironment,
    private val supportedTypes: SupportedTypes
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .mapNotNull(this::createDescriptor)
            .map(::SerializerGenerator)
            .map(SerializerGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): SerializerDescriptor? {
        val attributes = mutableSetOf<DestinationAttribute>()
        val keys = mutableSetOf<DestinationAttributeKey>()

        if (!element.isKotlinObject) {
            val constructor = element.getCompassConstructor()

            constructor.parameters.forEach { attr ->
                if (!supportedTypes.isSupported(attr)) {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                        "unsupported parameter $element -> ${attr.simpleName}", attr)
                    return null
                }

                val simpleName = attr.simpleName.toString()

                val keyName = "KEY_" + CaseFormat.LOWER_CAMEL.converterTo(
                    CaseFormat.UPPER_UNDERSCORE
                ).convert(simpleName)!!
                val keyValue = element.asType().toString() + "." + attr.simpleName

                attributes.add(
                    DestinationAttribute(attr, simpleName, keyName, supportedTypes.get(attr))
                )

                keys.add(
                    DestinationAttributeKey(keyName, keyValue)
                )
            }
        }

        return SerializerDescriptor(
            element,
            element.packageName(processingEnv),
            element.asClassName(),
            element.serializerClassName(),
            element.isKotlinObject,
            attributes,
            keys
        )
    }
}