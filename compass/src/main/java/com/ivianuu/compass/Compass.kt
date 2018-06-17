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

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import java.lang.reflect.Method

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Suppress("UNCHECKED_CAST")
object Compass {

    private const val METHOD_NAME_GET = "get"
    private const val SUFFIX_DETOUR_PROVIDER = "__DetourProvider"
    private const val SUFFIX_ROUTE_PROVIDER = "__RouteProvider"
    private const val SUFFIX_SERIALIZER_PROVIDER = "__SerializerProvider"

    private val routeMethods = mutableMapOf<Class<*>, Method>()
    private val detourMethods = mutableMapOf<Class<*>, Method>()
    private val serializerMethods = mutableMapOf<Class<*>, Method>()

    private val unexistingClasses = mutableSetOf<String>()

    fun <T : Any> requireActivityDetour(destination: T) =
        getFragmentDetour(destination) ?: throw IllegalArgumentException("no activity detour found for $destination")

    fun <T : Any> getActivityDetour(destination: T): ActivityDetour<T>? =
        getDetour(ActivityDetour::class.java, destination)
                as? ActivityDetour<T>

    fun <T : Any> requireFragmentDetour(destination: T) =
        getFragmentDetour(destination) ?: throw IllegalArgumentException("no fragment detour found for $destination")

    fun <T : Any> getFragmentDetour(destination: T): FragmentDetour<T>? =
        getDetour(FragmentDetour::class.java, destination)
                as? FragmentDetour<T>

    inline fun <reified T : CompassDetour> getDetour(destination: Any): T? =
            getDetour(T::class.java, destination)

    fun <T : CompassDetour> getDetour(clazz: Class<T>, destination: Any): T? {
        val detourProviderClass = findClazz(
            destination::class.java.name + SUFFIX_DETOUR_PROVIDER,
            destination::class.java.classLoader
        ) ?: return null

        val method = findMethod(detourProviderClass, METHOD_NAME_GET, detourMethods)
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

    fun requireIntent(context: Context, destination: Any): Intent =
        getIntent(context, destination) ?: throw IllegalArgumentException("no intent found for $destination")

    fun getIntent(context: Context, destination: Any): Intent? {
        val routeFactory =
            getRouteFactory(ActivityRouteFactory::class.java, destination)
                    as? ActivityRouteFactory<Any>
                    ?: return null

        val intent = routeFactory.createIntent(context, destination)

        val serializer = getSerializer(destination)
        if (serializer != null) {
            intent.putExtras(serializer.toBundle(destination))
        }

        return intent
    }

    fun <T : Fragment> requireFragment(destination: Any) =
        getFragment<T>(destination) ?: throw IllegalStateException("no fragment found for $destination")

    fun <T : Fragment> getFragment(destination: Any): T? {
        val routeFactory =
            getRouteFactory(FragmentRouteFactory::class.java, destination)
                    as? FragmentRouteFactory<Any>
                    ?: return null

        val fragment = routeFactory.createFragment(destination) as? T

        if (fragment != null) {
            val serializer = getSerializer(destination)
            if (serializer != null) {
                fragment.arguments = serializer.toBundle(destination)
            }
        }

        return fragment
    }

    inline fun <reified T : CompassRouteFactory> getRouteFactory(destination: Any) =
            getRouteFactory(T::class.java, destination)

    fun <T : CompassRouteFactory> getRouteFactory(clazz: Class<T>, destination: Any): T? {
        val routeProviderClass = findClazz(
            destination::class.java.name + SUFFIX_ROUTE_PROVIDER,
            destination::class.java.classLoader
        ) ?: return null


        val method = findMethod(routeProviderClass, METHOD_NAME_GET, routeMethods)

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

    fun <T : Any> getSerializer(destination: T): CompassSerializer<T>? {
        val serializerProviderClass = findClazz(
            destination::class.java.name + SUFFIX_SERIALIZER_PROVIDER,
            destination::class.java.classLoader
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

    fun <T : Any> requireSerializer(destination: T) =
        getSerializer(destination)
                ?: throw IllegalStateException("no serializer found for $destination")

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