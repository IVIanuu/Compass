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
import com.google.common.base.CaseFormat
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.compiler.util.packageName
import com.ivianuu.compass.compiler.util.serializerClassName
import com.ivianuu.compass.compiler.util.shouldBeSerialized
import com.ivianuu.processingx.hasAnnotation
import com.ivianuu.processingx.write
import com.squareup.kotlinpoet.asClassName
import org.jetbrains.annotations.Nullable
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

// todo check if destination is a public class etc
class SerializerProcessingStep(
    private val processingEnv: ProcessingEnvironment,
    private val supportedTypes: SupportedTypes
) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        val elements = mutableSetOf<Element>()
        elements.addAll(elementsByAnnotation[Destination::class.java])

        elements
            .asSequence()
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .mapNotNull(this::createDescriptor)
            .map(::SerializerGenerator)
            .map(SerializerGenerator::generate)
            .toList()
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): SerializerDescriptor? {
        val attributes = mutableSetOf<SerializerAttribute>()
        val keys = mutableSetOf<SerializerAttributeKey>()

        val constructorSelector = ConstructorSelector(processingEnv)

        val constructor = constructorSelector.getCompassConstructor(element) ?: return null

        constructor.parameters.forEach { attr ->
            if (!supportedTypes.isSupported(attr)) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "unsupported parameter $element -> ${attr.simpleName}", attr
                )
                return null
            }

            val simpleName = attr.simpleName.toString()

            val keyName = "KEY_" + CaseFormat.LOWER_CAMEL.converterTo(
                CaseFormat.UPPER_UNDERSCORE
            ).convert(simpleName)!!

            // find the field for the constructor element
            // to check if a @key annotation is present
            val field = element.enclosedElements
                .asSequence()
                .filterIsInstance<VariableElement>()
                .firstOrNull { it.simpleName.toString() == simpleName }

            val keyValue = element.asType().toString() + "." + attr.simpleName

            val isNullable = attr.hasAnnotation<Nullable>()

            attributes.add(
                SerializerAttribute(
                    attr, simpleName, keyName, supportedTypes.get(attr),
                    isNullable, false
                )
            )

            keys.add(SerializerAttributeKey(keyName, keyValue))
        }

        return SerializerDescriptor(
            element,
            element.packageName(),
            element.asClassName(),
            element.serializerClassName(),
            attributes,
            keys
        )
    }
}