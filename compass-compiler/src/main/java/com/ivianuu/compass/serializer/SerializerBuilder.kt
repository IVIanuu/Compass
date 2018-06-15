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

import com.ivianuu.compass.util.CLASS_BUNDLE
import com.ivianuu.compass.util.getCompassConstructor
import com.ivianuu.compass.util.isKotlinObject
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

object SerializerBuilder {

    fun addToBundleMethod(
        environment: ProcessingEnvironment,
        builder: TypeSpec.Builder,
        element: TypeElement
    ): TypeSpec.Builder {
        val serializer = AttributeSerializer(environment)

        val funBuilder = FunSpec.builder("toBundle")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", element.asClassName())
            .returns(CLASS_BUNDLE)

        funBuilder.addStatement("val bundle = %T()", CLASS_BUNDLE)

        if (!element.isKotlinObject) {
            val constructor = element.getCompassConstructor()
            constructor.parameters
                .forEach { attribute ->

                    if (!SupportedTypes.isSupported(attribute)) {
                        environment.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "unsupported type $attribute", element
                        )
                    }

                    environment.messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "test type ${attribute.asType()} for -> ${element.asType()}"
                    )

                    serializer.createBundlePut(
                        funBuilder, attribute, element, attribute.simpleName.toString())
                }
        }

        funBuilder.addStatement("return bundle")

        return builder.addFunction(funBuilder.build())

    }

    fun addFromBundleMethod(
        environment: ProcessingEnvironment,
        builder: TypeSpec.Builder,
        element: TypeElement
    ): TypeSpec.Builder {
        val serializer = AttributeSerializer(environment)

        val funBuilder = FunSpec.builder("fromBundle")
            .addModifiers(KModifier.OVERRIDE)
            .returns(element.asClassName())
            .addParameter("bundle", CLASS_BUNDLE)

        if (!element.isKotlinObject) {
            val compassConstructor = element.getCompassConstructor()

            val valueNames = mutableListOf<String>()

            compassConstructor.parameters
                .forEach { attribute ->
                    val valueName = attribute.simpleName.toString()
                    valueNames.add(valueName)

                    if (!SupportedTypes.isSupported(attribute)) {
                        environment.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "unsupported type $attribute", element
                        )
                    }

                    serializer.createBundleGet(
                        funBuilder, attribute, element, attribute.simpleName.toString())
                }

            funBuilder.addCode("\n")
            val constructorStatement = "return %T(${
            compassConstructor.parameters
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