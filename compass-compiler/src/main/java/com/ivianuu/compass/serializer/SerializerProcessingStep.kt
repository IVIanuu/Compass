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
import com.google.auto.common.MoreElements
import com.google.common.base.CaseFormat
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Key
import com.ivianuu.compass.Serialize
import com.ivianuu.compass.Serializer
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.asClassName
import org.jetbrains.annotations.Nullable
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

class SerializerProcessingStep(
    private val processingEnv: ProcessingEnvironment,
    private val supportedTypes: SupportedTypes
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        val elements = mutableSetOf<Element>()
        elements.addAll(elementsByAnnotation[Destination::class.java])
        elements.addAll(elementsByAnnotation[Serialize::class.java])

        elements
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .mapNotNull(this::createDescriptor)
            .map(::SerializerGenerator)
            .map(SerializerGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java, Serialize::class.java)

    private fun createDescriptor(element: TypeElement): SerializerDescriptor? {
        if (MoreElements.isAnnotationPresent(element, Serializer::class.java)) {
            return null
        }

        val attributes = mutableSetOf<SerializerAttribute>()
        val keys = mutableSetOf<SerializerAttributeKey>()

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

                // find the field for the constructor element
                // to check if a @key annotation is present
                val field = element.enclosedElements
                    .filterIsInstance<VariableElement>()
                    .firstOrNull { it.simpleName.toString() == simpleName }

                val keyValue = if (field != null
                    && MoreElements.isAnnotationPresent(field, Key::class.java)
                ) {
                    val value = field.getAnnotation(Key::class.java).value
                    if (value.isEmpty()) {
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "key value must be not empty",
                            field
                        )
                        return null
                    }
                    value
                } else {
                    element.asType().toString() + "." + attr.simpleName
                }

                val isNullable = MoreElements.isAnnotationPresent(attr, Nullable::class.java)

                attributes.add(
                    SerializerAttribute(
                            attr, simpleName, keyName, supportedTypes.get(attr),
                            isNullable, false
                        )
                    )

                keys.add(SerializerAttributeKey(keyName, keyValue))
            }
        }

        return SerializerDescriptor(
            element,
            element.packageName(),
            element.asClassName(),
            element.serializerClassName(),
            element.isKotlinObject,
            attributes,
            keys
        )
    }
}