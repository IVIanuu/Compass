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

import com.ivianuu.compass.util.CLASS_ACTIVITY_ROUTE_FACTORY
import com.ivianuu.compass.util.CLASS_FRAGMENT_ROUTE_FACTORY
import com.ivianuu.compass.util.destinationTarget
import com.ivianuu.compass.util.isSubtypeOfType
import com.squareup.kotlinpoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author Manuel Wrage (IVIanuu)
 */
object RouteFactoryBuilder {

    fun buildRouteFactory(
        environment: ProcessingEnvironment,
        element: TypeElement
    ): TypeSpec? {
        val target = element.destinationTarget

        if (target != null && target.toString() != "java.lang.Void") {
            val targetAsType = environment.elementUtils.getTypeElement(target.toString())

            val hasEmptyConstructor = targetAsType.enclosedElements
                .filterIsInstance<ExecutableElement>()
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .all { it.parameters.isEmpty() }

            if (!hasEmptyConstructor) {
                environment.messager.printMessage(Diagnostic.Kind.ERROR, "route factory must have a empty constructor")
            }

            return when {
                isSubtypeOfType(target, "android.support.v4.app.Fragment") -> {
                    buildFragmentFactory(element, target.toString())
                }
                isSubtypeOfType(target, "android.app.Activity") -> {
                    buildActivityFactory(element, target.toString())
                }
                else -> {
                    environment.messager.printMessage(
                        Diagnostic.Kind.ERROR, "$target -> only fragments or activities are supported for now")
                    null
                }
            }
        }

        return null
    }

    private fun buildFragmentFactory(element: TypeElement,
                                     target: String): TypeSpec {
        val type = TypeSpec.objectBuilder("${element.simpleName}__RouteFactory")
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_FRAGMENT_ROUTE_FACTORY,
                    element.asClassName()
                )
            )

        val createBuilder = FunSpec.builder("createFragment")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", element.asClassName())
            .returns(ClassName.bestGuess("android.support.v4.app.Fragment"))
            .addStatement("return %T()", ClassName.bestGuess(target))
            .build()

        type.addFunction(createBuilder)

        return type.build()
    }

    private fun buildActivityFactory(element: TypeElement,
                                     target: String): TypeSpec {
        val type = TypeSpec.classBuilder("${element.simpleName}__RouteFactory")
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_ACTIVITY_ROUTE_FACTORY,
                    element.asClassName()
                )
            )

        val createBuilder = FunSpec.builder("createIntent")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", element.asClassName())
            .returns(ClassName.bestGuess("android.content.Intent"))
            .addStatement("return %T(context, %T::class)", ClassName.bestGuess("android.content.Intent"), ClassName.bestGuess(target))
            .build()

        type.addFunction(createBuilder)

        return type.build()
    }
}