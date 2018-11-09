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

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

class ConstructorSelector(private val processingEnv: ProcessingEnvironment) {

    fun getCompassConstructor(element: Element): ExecutableElement? {
        val constructors = ElementFilter.constructorsIn(element.enclosedElements)
            .asSequence()
            .filter { it.modifiers.contains(Modifier.PUBLIC) }
            .asSequence()
            .toList()

        if (constructors.isEmpty()) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR, "must have a public constructor", element
            )
            return null
        }

        val bestConstructor = constructors.asSequence()
            .sortedByDescending { it.parameters.size }
            .filter { constructor -> constructor.parameters.all { it.hasAccessor(element) } }
            .firstOrNull()

        if (bestConstructor != null) {
            return bestConstructor
        } else {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "No suitable constructor found", element
            )
            return null
        }
    }

    private fun VariableElement.hasAccessor(base: Element): Boolean {
        val containsFieldAccessor = ElementFilter
            .fieldsIn(base.enclosedElements)
            .asSequence()
            .filter { it.modifiers.contains(Modifier.PUBLIC) }
            .filter { it.simpleName == this.simpleName }
            .any()

        val containsMethodAccessor = ElementFilter
            .methodsIn(base.enclosedElements)
            .asSequence()
            .filter { it.modifiers.contains(Modifier.PUBLIC) }
            .filter { it.returnType.toString() == this.asType().toString() }
            .filter { it.parameters.isEmpty() }
            .filter {
                it.simpleName == this.simpleName
                        || it.simpleName.toString() ==
                        "get${this.simpleName.toString().capitalize()}"
            }
            .any()

        return containsFieldAccessor || containsMethodAccessor
    }

}