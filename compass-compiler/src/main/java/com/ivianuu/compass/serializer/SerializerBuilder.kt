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

import com.ivianuu.compass.attribute.attributeSerializers
import com.ivianuu.compass.util.CLASS_BUNDLE
import com.ivianuu.compass.util.getCompassConstructor
import com.ivianuu.compass.util.isKotlinObject
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

object SerializerBuilder {

    const val PARAM_BUNDLE = "bundle"
    const val PARAM_DESTINATION = "destination"

    const val METHOD_NAME_FROM_BUNDLE = "readFromBundle"
    const val METHOD_NAME_TO_BUNDLE = "writeToBundle"

    private val logicParts = attributeSerializers()

    fun addToBundleMethod(
        environment: ProcessingEnvironment,
        builder: TypeSpec.Builder,
        element: TypeElement
    ): TypeSpec.Builder {
        val funBuilder = FunSpec.builder(METHOD_NAME_TO_BUNDLE)
            .addAnnotation(JvmStatic::class)
            .addParameter(PARAM_DESTINATION, element.asClassName())
            .addParameter(PARAM_BUNDLE, CLASS_BUNDLE)

        if (!element.isKotlinObject) {
            val constructor = element.getCompassConstructor()
            constructor.parameters.asSequence()
                .forEachIndexed { index, attribute ->
                    if (index > 0) funBuilder.addCode("\n\n")
                    val handled = logicParts.asSequence()
                        .map {
                            it.addAttributeSerializeLogic(
                                environment, funBuilder,
                                element, attribute
                            )
                        }
                        .filter { it }
                        .toList()
                        .isNotEmpty()

                    if (!handled) environment.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "unsupported type $attribute"
                    )
                }
        }

        return builder.addFunction(funBuilder.build())

    }

    fun addFromBundleMethod(
        environment: ProcessingEnvironment,
        builder: TypeSpec.Builder,
        element: TypeElement
    ): TypeSpec.Builder {
        val funBuilder = FunSpec.builder(METHOD_NAME_FROM_BUNDLE)
            .addAnnotation(JvmStatic::class)
            .returns(element.asClassName())
            .addParameter(PARAM_BUNDLE, CLASS_BUNDLE)

        if (!element.isKotlinObject) {
            val CompassConstructor = element.getCompassConstructor()

            val valueNames = mutableListOf<String>()

            CompassConstructor.parameters
                .asSequence()
                .forEach { attribute ->
                    val valueName = attribute.simpleName.toString()
                    valueNames.add(valueName)
                    val handled = logicParts.asSequence()
                        .map {
                            it.addBundleAccessorLogic(
                                environment,
                                funBuilder, element, attribute, valueName
                            )
                        }
                        .filter { it }
                        .toList().isNotEmpty()

                    if (!handled) environment.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "unsupported type $attribute"
                    )
                }

            funBuilder.addCode("\n")
            val constructorStatement = "return %T(${
            CompassConstructor.parameters
                .asSequence()
                .withIndex()
                .map { valueNames[it.index] }
                .joinToString(", ")
            })"
            funBuilder.addStatement(constructorStatement, element.asType())
        } else {
            funBuilder.addStatement("return %T", element.asType())
        }

        return builder.addFunction(funBuilder.build())
    }

}