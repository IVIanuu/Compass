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

import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Access point for generated classes
 */
object Compass {

    private const val METHOD_NAME_GET = "get"
    private const val SUFFIX_DETOUR_PROVIDER = "__DetourProvider"
    private const val SUFFIX_ROUTE_PROVIDER = "__RouteProvider"
    private const val SUFFIX_SERIALIZER_PROVIDER = "__SerializerProvider"

    private val routeMethods = mutableMapOf<Class<*>, Method>()
    private val detourMethods = mutableMapOf<Class<*>, Method>()
    private val serializerMethods = mutableMapOf<Class<*>, Method>()

    private val unexistingClasses = mutableSetOf<String>()

    fun <T : Any> requireDetour(destinationClass: KClass<out T>) =
        getDetour(destinationClass)
            ?: throw IllegalStateException("no detour found for $destinationClass")

    fun <T : Any> getDetour(destinationClass: KClass<out T>): CompassDetour? {
        val detourProviderClass = findClazz(
            destinationClass.java.name.replace("\$", "_") + SUFFIX_DETOUR_PROVIDER,
            destinationClass.java.classLoader
        ) ?: return null

        val method = findMethod(detourProviderClass, METHOD_NAME_GET, detourMethods)
        if (method != null) {
            try {
                return method.invoke(null) as CompassDetour
            } catch (e: Exception) {
            }
        }

        return null
    }

    fun <T : Any> requireRouteFactory(destinationClass: KClass<T>) =
        getRouteFactory(destinationClass)
                ?: throw IllegalStateException("no route factory found for $destinationClass")

    fun <T : Any> getRouteFactory(destinationClass: KClass<T>): CompassRouteFactory? {
        val routeProviderClass = findClazz(
            destinationClass.java.name.replace("\$", "_") + SUFFIX_ROUTE_PROVIDER,
            destinationClass.java.classLoader
        ) ?: return null


        val method = findMethod(routeProviderClass, METHOD_NAME_GET, routeMethods)

        if (method != null) {
            try {
                return method.invoke(null) as CompassRouteFactory
            } catch (e: Exception) {
            }
        }

        return null
    }

    fun <T : Any> requireSerializer(destinationClass: KClass<out T>) =
        getSerializer(destinationClass)
                ?: throw IllegalStateException("no serializer found for $destinationClass")

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getSerializer(destinationClass: KClass<out T>): CompassSerializer<T>? {
        val serializerProviderClass = findClazz(
            destinationClass.java.name.replace("\$", "_") + SUFFIX_SERIALIZER_PROVIDER,
            destinationClass.java.classLoader
        ) ?: return null

        val method = findMethod(serializerProviderClass, METHOD_NAME_GET, serializerMethods)

        if (method != null) {
            try {
                return method.invoke(null) as CompassSerializer<T>
            } catch (e: Exception) {
            }
        }

        return null
    }

    private fun findClazz(
        className: String,
        classLoader: ClassLoader?
    ): Class<*>? {
        if (unexistingClasses.contains(className)) return null

        return try {
            classLoader?.loadClass(className)
        } catch (e: Exception) {
            unexistingClasses.add(className)
            null
        }
    }

    private fun findMethod(
        clazz: Class<*>,
        methodName: String,
        map: MutableMap<Class<*>, Method>
    ): Method? {
        var method = map[clazz]
        if (method != null) {
            return method
        }

        return try {
            method = clazz.declaredMethods.firstOrNull { it.name == methodName }
            if (method != null) {
                map[clazz] = method
            }
            method
        } catch (e: Exception) {
            null
        }
    }

}