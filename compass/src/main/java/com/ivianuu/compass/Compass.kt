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

    inline fun <reified T : Any> requireActivityDetour() =
        requireActivityDetour(T::class.java)

    fun <T : Any> requireActivityDetour(destination: T) =
        getActivityDetour(destination::class.java)

    fun <T : Any> requireActivityDetour(destinationClass: Class<T>) =
        getActivityDetour(destinationClass)
                ?: throw IllegalArgumentException("no activity detour found for $destinationClass")

    inline fun <reified T : Any> getActivityDetour() =
        getActivityDetour(T::class.java)

    fun <T : Any> getActivityDetour(destination: T) =
        getActivityDetour(destination::class.java) as? ActivityDetour<T>

    fun <T : Any> getActivityDetour(destinationClass: Class<T>) =
        getDetour(ActivityDetour::class.java, destinationClass) as? ActivityDetour<T>

    inline fun <reified T : Any> requireFragmentDetour() =
        requireFragmentDetour(T::class.java)

    fun <T : Any> requireFragmentDetour(destination: T) =
        requireFragmentDetour(destination::class.java)

    fun <T : Any> requireFragmentDetour(destinationClass: Class<T>) =
        getFragmentDetour(destinationClass)
                ?: throw IllegalArgumentException("no fragment detour found for $destinationClass")

    inline fun <reified T : Any> getFragmentDetour() =
        getFragmentDetour(T::class.java)

    fun <T : Any> getFragmentDetour(destination: T) =
        getFragmentDetour(destination::class.java) as? FragmentDetour<T>

    fun <T : Any> getFragmentDetour(destinationClass: Class<T>) =
        getDetour(FragmentDetour::class.java, destinationClass) as? FragmentDetour<T>

    inline fun <reified T : CompassDetour, reified D : Any> requireDetour() =
        requireDetour(T::class.java, D::class.java)

    inline fun <reified T : CompassDetour> requireDetour(destination: Any) =
        requireDetour(T::class.java, destination)

    fun <T : CompassDetour> requireDetour(detourClass: Class<T>, destination: Any) =
        requireDetour(detourClass, destination::class.java)

    fun <T : CompassDetour> requireDetour(detourClass: Class<T>, destinationClass: Class<*>) =
        getDetour(detourClass, destinationClass)
                ?: throw IllegalStateException("no detour found for $destinationClass")

    inline fun <reified T : CompassDetour> getDetour(destination: Any) =
            getDetour(T::class.java, destination)

    inline fun <reified T : CompassDetour, reified D : Any> getDetour() =
        getDetour(T::class.java, D::class.java)

    fun <T : CompassDetour> getDetour(clazz: Class<T>, destination: Any) =
        Compass.getDetour(clazz, destination::class.java)

    fun <T : CompassDetour> getDetour(detourClass: Class<T>, destinationClass: Class<*>): T? {
        val detourProviderClass = findClazz(
            destinationClass.name + SUFFIX_DETOUR_PROVIDER,
            destinationClass.classLoader
        ) ?: return null

        val method = findMethod(detourProviderClass, METHOD_NAME_GET, detourMethods)
        if (method != null) {
            try {
                return detourClass.cast(method.invoke(null))
            } catch (e: Exception) {
            }
        }

        return null
    }

    fun requireIntent(context: Context, destination: Any) =
        getIntent(context, destination) ?: throw IllegalArgumentException("no intent found for $destination")

    fun getIntent(context: Context, destination: Any): Intent? {
        val routeFactory =
            getRouteFactory(ActivityRouteFactory::class.java, destination)
                    as? ActivityRouteFactory<Any>
                    ?: return null

        val intent = routeFactory.createActivityIntent(context, destination)

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

    inline fun <reified T : CompassRouteFactory> requireRouteFactory(destination: Any) =
        requireRouteFactory(T::class.java, destination)

    fun <T : CompassRouteFactory> requireRouteFactory(
        routeFactoryClass: Class<T>,
        destination: Any
    ) =
        requireRouteFactory(routeFactoryClass, destination::class.java)

    fun <T : CompassRouteFactory> requireRouteFactory(
        routeFactoryClass: Class<T>,
        destinationClass: Class<*>
    ) =
        getRouteFactory(routeFactoryClass, destinationClass)
                ?: throw IllegalStateException("no route factory found for $destinationClass")



    inline fun <reified T : CompassRouteFactory> getRouteFactory(destination: Any) =
            getRouteFactory(T::class.java, destination)

    fun <T : CompassRouteFactory> getRouteFactory(routeFactoryClass: Class<T>, destination: Any) =
        getRouteFactory(routeFactoryClass, destination::class.java)

    fun <T : CompassRouteFactory> getRouteFactory(
        routeFactoryClass: Class<T>,
        destinationClass: Class<*>
    ): T? {
        val routeProviderClass = findClazz(
            destinationClass.name + SUFFIX_ROUTE_PROVIDER,
            destinationClass.classLoader
        ) ?: return null


        val method = findMethod(routeProviderClass, METHOD_NAME_GET, routeMethods)

        if (method != null) {
            try {
                return routeFactoryClass.cast(method.invoke(null))
            } catch (e: Exception) {
            }
        }

        return null
    }


    inline fun <reified T : Any> requireSerializer() =
        requireSerializer(T::class.java)

    fun <T : Any> requireSerializer(destination: T) =
        requireSerializer(destination::class.java)

    fun <T : Any> requireSerializer(destinationClass: Class<T>) =
        getSerializer(destinationClass)
                ?: throw IllegalStateException("no serializer found for $destinationClass")


    inline fun <reified T : Any> getSerializer() =
        getSerializer(T::class.java)

    fun <T : Any> getSerializer(destination: T) =
        getSerializer(destination::class.java) as? CompassSerializer<T>?

    fun <T : Any> getSerializer(destinationClass: Class<T>): CompassSerializer<T>? {
        val serializerProviderClass = findClazz(
            destinationClass.name + SUFFIX_SERIALIZER_PROVIDER,
            destinationClass.classLoader
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