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

package com.ivianuu.compass.detour

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.common.MoreElements
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour
import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author Manuel Wrage (IVIanuu)
 */
class DetourProviderProcessingStep(private val processingEnv: ProcessingEnvironment) : BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Detour::class.java]
            .filterIsInstance<TypeElement>()
            .mapNotNull(this::createDescriptor)
            .map(::DetourProviderGenerator)
            .map(DetourProviderGenerator::generate)
            .forEach { it.write(processingEnv) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Detour::class.java)

    private fun createDescriptor(element: TypeElement): DetourProviderDescriptor? {
        if (!MoreElements.isAnnotationPresent(element, Destination::class.java)) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "missing @Destination annotation", element)
            return null
        }

        val detour = element.detourClass?.let {
            processingEnv.elementUtils.getTypeElement(it.toString())
        }

        if (detour == null) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "couldn't resolve detour", element)
            return null
        }


        val hasEmptyConstructor = detour.enclosedElements
            .filterIsInstance<ExecutableElement>()
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .all { it.parameters.isEmpty() }

        if (!hasEmptyConstructor) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "detour must have a empty constructor", element)
            return null
        }

        return DetourProviderDescriptor(
            element,
            element.packageName(processingEnv),
            element.detourProviderClassName(),
            detour.asClassName(),
            detour.isKotlinObject
        )
    }
}