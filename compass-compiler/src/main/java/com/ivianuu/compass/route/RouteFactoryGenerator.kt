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

import com.ivianuu.compass.util.*
import com.squareup.kotlinpoet.*

class RouteFactoryGenerator(private val descriptor: RouteFactoryDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName,
            descriptor.routeFactory.simpleName())

        if (descriptor.targetType == TargetType.ACTIVITY) {
            file.addType(activityRouteFactory())
        } else if (descriptor.targetType == TargetType.FRAGMENT) {
            file.addType(fragmentRouteFactory())
        }

        return file.build()
    }

    private fun fragmentRouteFactory(): TypeSpec {
        val type = TypeSpec.objectBuilder(descriptor.routeFactory)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_FRAGMENT_ROUTE_FACTORY,
                    descriptor.destination
                )
            )

        val createBuilder = FunSpec.builder("createFragment")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", descriptor.destination)
            .returns(CLASS_FRAGMENT)
            .addStatement("return %T()", descriptor.target)
            .build()

        type.addFunction(createBuilder)

        return type.build()
    }

    private fun activityRouteFactory(): TypeSpec {
        val type = TypeSpec.classBuilder(descriptor.routeFactory)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_ACTIVITY_ROUTE_FACTORY,
                    descriptor.destination
                )
            )

        val createBuilder = FunSpec.builder("createIntent")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", descriptor.destination)
            .returns(CLASS_INTENT)
            .addStatement("return %T(context, %T::class)", CLASS_INTENT, descriptor.target)
            .build()

        type.addFunction(createBuilder)

        return type.build()
    }

}