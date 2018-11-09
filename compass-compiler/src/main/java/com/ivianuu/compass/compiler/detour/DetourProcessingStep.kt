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

package com.ivianuu.compass.compiler.detour

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour
import com.ivianuu.compass.compiler.util.detourClass
import com.ivianuu.compass.compiler.util.hasEmptyConstructor
import com.ivianuu.processingx.hasAnnotation
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class DetourProcessingStep(private val processingEnv: ProcessingEnvironment) :
    BasicAnnotationProcessor.ProcessingStep {

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): MutableSet<Element> {
        elementsByAnnotation[Detour::class.java]
            .asSequence()
            .filterIsInstance<TypeElement>()
            .forEach { validate(it) }

        return mutableSetOf()
    }

    override fun annotations() = mutableSetOf(Detour::class.java)

    private fun validate(element: TypeElement) {
        if (!element.hasAnnotation<Destination>()) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "missing @Destination annotation", element)
            return
        }

        val detourType = element.detourClass?.let {
            processingEnv.elementUtils.getTypeElement(it.toString())
        }

        if (detourType == null) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "couldn't resolve detour", element)
            return
        }

        if (!detourType.hasEmptyConstructor) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "detour must have a empty constructor", element)
            return
        }
    }
}