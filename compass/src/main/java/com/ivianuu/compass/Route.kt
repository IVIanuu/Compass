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

package com.ivianuu.compass

import android.content.Intent
import kotlin.reflect.KClass

/**
 * Creates route components e.g [Intent] or [Fragment] from a specific destination
 */
interface CompassRouteFactory

private const val SUFFIX_ROUTE_FACTORY = "__RouteFactory"
private val routeFactories = mutableMapOf<Class<*>, Class<*>>()

/**
 * Returns a new [CompassRouteFactory] associated with the [destinationClass] or throws
 */
fun <T : CompassRouteFactory> routeFactory(destinationClass: KClass<*>): T {
    val routeFactoryClass = routeFactories.getOrPut(destinationClass.java) {
        val generatedRouteFactory = if (destinationClass.java
                    .getAnnotation(Destination::class.java)?.target != Nothing::class
        ) {
            findClazz(
                destinationClass.java.name.replace("\$", "_") + SUFFIX_ROUTE_FACTORY,
                destinationClass.java.classLoader
            )
        } else {
            null
        }

        val routeFactoryAnnotationClass = destinationClass.java
            .getAnnotation(RouteFactory::class.java)?.clazz

        generatedRouteFactory
            ?: routeFactoryAnnotationClass?.java
            ?: throw IllegalArgumentException("either a @Destination.target or a @RouteFactory annotation must be provided")
    }

    return routeFactoryClass.newInstance() as T
}

/**
 * Returns a new [CompassRouteFactory] associated with [this] or throws
 */
fun <T : CompassRouteFactory> Any.routeFactory() = routeFactory<T>(this::class)

/**
 * Returns a new [CompassRouteFactory] associated with the [destinationClass] or null
 */
fun <T : CompassRouteFactory> routeFactoryOrNull(destinationClass: KClass<*>) = try {
    routeFactory<T>(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [CompassRouteFactory] associated with the [this] or null
 */
fun <T : CompassRouteFactory> Any.routeFactoryOrNull() = routeFactoryOrNull<T>(this::class)