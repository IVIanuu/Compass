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

package com.ivianuu.compass.route

import com.ivianuu.compass.util.destinationTarget
import com.ivianuu.compass.util.isKotlinObject
import com.ivianuu.compass.util.routeFactoryClass
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author Manuel Wrage (IVIanuu)
 */
object RouteProviderBuilder {

    fun buildRouteProvider(
        environment: ProcessingEnvironment,
        element: TypeElement
    ): TypeSpec {
        val target = element.destinationTarget
        val routeFactory = element.routeFactoryClass

        val isKotlinObject = if (routeFactory != null) {
            val type = environment.elementUtils.getTypeElement(routeFactory.toString())
            type.isKotlinObject
        } else {
            true
        }

        val factoryName = when {
            routeFactory != null -> routeFactory.toString()
            target != null -> element.asType().toString() + "RouteFactory"
            else -> null
        }

        val type = TypeSpec.objectBuilder("${element.simpleName}RouteProvider")

        if (factoryName == null) {
            environment.messager.printMessage(
                Diagnostic.Kind.ERROR, "either a target or a routeFactory must be specified")
            return type.build()
        }

        val getBuilder = FunSpec.builder("get")
            .addAnnotation(JvmStatic::class)
            .returns(ClassName.bestGuess(factoryName))
            .addCode(
                CodeBlock.builder()
                    .apply {
                        if (isKotlinObject) {
                            addStatement("return %T", ClassName.bestGuess(factoryName))
                        } else {
                            addStatement("return %T()", ClassName.bestGuess(factoryName))
                        }
                    }
                    .build()
            )

        type.addFunction(getBuilder.build())

        return type.build()
    }

}