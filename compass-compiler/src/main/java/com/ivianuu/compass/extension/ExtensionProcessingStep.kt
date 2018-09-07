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

package com.ivianuu.compass.extension

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.common.MoreElements
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Serializer
import com.ivianuu.compass.util.destinationTarget
import com.ivianuu.compass.util.packageName
import com.ivianuu.compass.util.serializerClass
import com.ivianuu.compass.util.serializerClassName
import com.ivianuu.compass.util.shouldBeSerialized
import com.ivianuu.compass.util.targetType
import com.ivianuu.compass.util.write
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class ExtensionProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Destination::class.java]
            .asSequence()
            .filterIsInstance<TypeElement>()
            .filter { it.shouldBeSerialized() }
            .mapNotNull(this::createDescriptor)
            .map(::ExtensionGenerator)
            .map(ExtensionGenerator::generate)
            .toList()
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() =
        mutableSetOf(Destination::class.java)

    private fun createDescriptor(element: TypeElement): ExtensionDescriptor? {
        val target = element.destinationTarget
        val targetAsType = (if (target != null) {
            processingEnv.elementUtils.getTypeElement(target.toString())
        } else {
            null
        }) ?: return null

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

        return ExtensionDescriptor(
            element,
            element.packageName(),
            element.asClassName(),
            targetAsType.asClassName(),
            targetAsType.targetType(processingEnv),
            element.simpleName.toString() + "Ext",
            serializerClassName
        )
    }
}