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

import android.os.Bundle
import java.lang.reflect.Method

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Suppress("UNCHECKED_CAST")
object Compass {

    @PublishedApi
    internal val fromBundleMethods = mutableMapOf<Class<*>, Method>()
    @PublishedApi
    internal val toBundleMethods = mutableMapOf<Class<*>, Method>()
    @PublishedApi
    internal val routeMethods = mutableMapOf<Class<*>, Method>()
    @PublishedApi
    internal val detourMethods = mutableMapOf<Class<*>, Method>()
    
    private val unexistingClasses = mutableSetOf<String>()

    inline fun <reified T : CompassDetour> getDetour(destination: Any): T? {
        val detourProviderClass = findClazz(
            destination::class.java.name + "DetourProvider",
            destination::class.java.classLoader
        ) ?: return null

        val method = findMethod(detourProviderClass, "get", detourMethods)
        if (method != null) {
            try {
                return method.invoke(null) as? T?
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    inline fun <reified T : CompassDetour> requireDetour(destination: Any) =
            getDetour<T>(destination) ?: throw IllegalStateException("no detour found for $destination")

    inline fun <reified T : Any> getRouteFactory(destination: Any): T? {
        val routeProviderClass = findClazz(
            destination::class.java.name + "RouteProvider",
            destination::class.java.classLoader
        ) ?: return null

        val method = findMethod(routeProviderClass, "get", routeMethods)
        if (method != null) {
            try {
                return method.invoke(null) as? T?
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    inline fun <reified T : Any> requireRouteFactory(destination: Any): T =
            getRouteFactory(destination) ?: throw IllegalStateException("no route factory found for $destination")

    fun toBundle(destination: Any, bundle: Bundle = Bundle()): Bundle {
        val toBundleMethod = findToBundleMethod(destination::class.java)
        if (toBundleMethod != null) {
            try {
                toBundleMethod.invoke(null, destination, bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return bundle
    }

    fun <T : Any> fromBundle(clazz: Class<T>, bundle: Bundle): T? {
        val method = findFromBundleMethod(clazz)
        if (method != null) {
            try {
                return method.invoke(null, bundle) as? T
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    private fun findFromBundleMethod(clazz: Class<*>): Method? {
        var method = fromBundleMethods[clazz]
        if (method != null) {
            return method
        }

        val clazzName = clazz.name

        try {
            val serializerClass = clazz.classLoader.loadClass(clazzName + "")
            method = serializerClass.getDeclaredMethod("readFromBundle", Bundle::class.java)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Unable to find fromBundle method for $clazzName", e)
        }

        fromBundleMethods[clazz] = method

        return method
    }

    private fun findToBundleMethod(clazz: Class<*>): Method? {
        var method = toBundleMethods[clazz]
        if (method != null) {
            return method
        }

        val clazzName = clazz.name

        try {
            val serializerClass = clazz.classLoader.loadClass(clazzName + "Serializer")
            method = serializerClass.getDeclaredMethod("writeToBundle", clazz, Bundle::class.java)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Unable to find fromBundle method for $clazzName", e)
        }

        toBundleMethods[clazz] = method

        return method
    }

    @PublishedApi
    internal fun findClazz(
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

    @PublishedApi
    internal fun findMethod(
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