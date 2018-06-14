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

import com.ivianuu.compass.util.CLASS_DETOUR
import com.ivianuu.compass.util.detourClass
import com.ivianuu.compass.util.isKotlinObject
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author Manuel Wrage (IVIanuu)
 */
object DetourProviderBuilder {

    fun buildDetourProvider(
        environment: ProcessingEnvironment,
        element: TypeElement
    ): TypeSpec? {
        val detour = element.detourClass ?: return null

        val detourAsType = environment.elementUtils.getTypeElement(detour.toString())

        val isKotlinObject = detourAsType.isKotlinObject

        val hasEmptyConstructor = detourAsType.enclosedElements
            .filterIsInstance<ExecutableElement>()
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .all { it.parameters.isEmpty() }

        if (!hasEmptyConstructor) {
            environment.messager.printMessage(Diagnostic.Kind.ERROR, "detour must have a empty constructor")
        }

        val type = TypeSpec.objectBuilder("${element.simpleName}DetourProvider")

        val getBuilder = FunSpec.builder("get")
            .addAnnotation(JvmStatic::class)
            .returns(CLASS_DETOUR)
            .addCode(
                CodeBlock.builder()
                    .apply {
                        if (isKotlinObject) {
                            addStatement("return %T", ClassName.bestGuess(detour.toString()))
                        } else {
                            addStatement("return %T()", ClassName.bestGuess(detour.toString()))
                        }
                    }
                    .build()
            )

        type.addFunction(getBuilder.build())

        return type.build()
    }

}