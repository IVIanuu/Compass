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

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Suppress("UNCHECKED_CAST")
object Compass {

    private val routeMethods = mutableMapOf<Class<*>, Method>()
    private val detourMethods = mutableMapOf<Class<*>, Method>()
    private val serializerMethods = mutableMapOf<Class<*>, Method>()

    private val unexistingClasses = mutableSetOf<String>()

    inline fun <reified T : CompassDetour> getDetour(destination: Any): T? =
            getDetour(T::class.java, destination)

    fun <T : CompassDetour> getDetour(clazz: Class<T>, destination: Any): T? {
        val detourProviderClass = findClazz(
            destination::class.java.name + "__DetourProvider",
            destination::class.java.classLoader
        ) ?: return null

        val method = findMethod(detourProviderClass, "get", detourMethods)
        if (method != null) {
            try {
                return clazz.cast(method.invoke(null))
            } catch (e: Exception) {
            }
        }

        return null
    }

    inline fun <reified T : CompassDetour> requireDetour(destination: Any) =
            requireDetour(T::class.java, destination)

    fun <T : CompassDetour> requireDetour(clazz: Class<T>, destination: Any) =
        getDetour(clazz, destination) ?: throw IllegalStateException("no detour found for $destination")


    inline fun <reified T : CompassRouteFactory> getRouteFactory(destination: Any) =
            getRouteFactory(T::class.java, destination)

    fun <T : CompassRouteFactory> getRouteFactory(clazz: Class<T>, destination: Any): T? {
        val routeProviderClass = findClazz(
            destination::class.java.name + "__RouteProvider",
            destination::class.java.classLoader
        ) ?: return null


        val method = findMethod(routeProviderClass, "get", routeMethods)

        if (method != null) {
            try {
                return clazz.cast(method.invoke(null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    fun <T : CompassRouteFactory> requireRouteFactory(clazz: Class<T>, destination: Any) =
        getRouteFactory(clazz, destination)
                ?: throw IllegalStateException("no route factory found for $destination")

    inline fun <reified T : CompassRouteFactory> requireRouteFactory(destination: Any) =
            requireRouteFactory(T::class.java, destination)


    inline fun <reified T : Any> getSerializer(destination: Any) =
        getSerializer(T::class.java, destination)

    fun <T : Any> getSerializer(clazz: Class<T>, destination: Any): CompassSerializer<T>? {
        val serializerProviderClass = findClazz(
            destination::class.java.name + "__SerializerProvider",
            destination::class.java.classLoader
        ) ?: return null

        val method = findMethod(serializerProviderClass, "get", serializerMethods)

        if (method != null) {
            try {
                return method.invoke(null) as CompassSerializer<T>
            } catch (e: Exception) {
            }
        }

        return null
    }

    fun <T : Any> requireSerializer(clazz: Class<T>, destination: Any) =
        getSerializer(clazz, destination)
                ?: throw IllegalStateException("no serializer found for $destination")

    inline fun <reified T : Any> requireSerializer(destination: Any) =
        requireSerializer(T::class.java, destination)

    private fun findClazz(
        className: String,
        classLoader: ClassLoader
    ): Class<*>? {
        if (unexistingClasses.contains(className)) return null

        return try {
            classLoader.loadClass(className)
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