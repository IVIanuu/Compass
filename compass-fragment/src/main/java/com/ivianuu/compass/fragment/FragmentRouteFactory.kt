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

package com.ivianuu.compass.fragment

import androidx.fragment.app.Fragment
import com.ivianuu.compass.CompassRouteFactory
import com.ivianuu.compass.Destination
import com.ivianuu.compass.routeFactory
import com.ivianuu.compass.serializerOrNull
import kotlin.reflect.KClass

/**
 * Creates [Fragment] from Returns a new
 */
interface FragmentRouteFactory<T : Any> : CompassRouteFactory {
    /**
     * Returns a new [Fragment] associated with the [destination]
     */
    fun createFragment(destination: T): Fragment
}

class ReflectFragmentRouteFactory<T : Any> : FragmentRouteFactory<T> {
    override fun createFragment(destination: T): Fragment {
        val fragmentClass = cachedFragmentClasses.getOrPut(destination.javaClass) {
            val annotation = destination.javaClass.getAnnotation(Destination::class.java)
                ?: throw IllegalArgumentException("missing @Destination annotation")

            val fragmentClass = annotation.target.java

            if (!Fragment::class.java.isAssignableFrom(fragmentClass)) {
                throw IllegalArgumentException("target is not a Fragment")
            }

            fragmentClass as Class<Fragment>
        }

        return try {
            fragmentClass.newInstance() as Fragment
        } catch (e: InstantiationException) {
            throw InstantiationException("failed to create fragment $fragmentClass, $e")
        }
    }

    private companion object {
        private val cachedFragmentClasses = mutableMapOf<Class<*>, Class<Fragment>>()
    }
}

/**
 * Returns a new [FragmentRouteFactory] associated with the [destinationClass] or throws
 */
fun <D : Any> fragmentRouteFactory(destinationClass: KClass<out D>) =
    routeFactory<FragmentRouteFactory<D>>(destinationClass)

/**
 * Returns a new [FragmentRouteFactory] associated with [this] or throws
 */
fun <T : Any> T.fragmentRouteFactory() = fragmentRouteFactory(this::class)

/**
 * Returns a new [FragmentRouteFactory] associated with the [destinationClass] or null
 */
fun <D : Any> fragmentRouteFactoryOrNull(destinationClass: KClass<out D>) = try {
    fragmentRouteFactory(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [FragmentRouteFactory] associated with [this] or null
 */
fun <T : Any> T.fragmentRouteFactoryOrNull() = fragmentRouteFactoryOrNull(this::class)

/**
 * Returns a new [Fragment] associated with [this]
 */
fun <D : Any> D.fragment(): Fragment {
    val routeFactory = fragmentRouteFactory()
    val fragment = routeFactory.createFragment(this)
    serializerOrNull()?.toBundle(this)?.let { fragment.arguments = it }
    return fragment
}

/**
 * Returns a new [Fragment] associated with [this] or null
 */
fun <D : Any> D.fragmentOrNull() = try {
    fragment()
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [F] associated this [this]
 */
inline fun <D : Any, reified F : Fragment> D.fragment(fragmentClass: KClass<F> = F::class) =
    fragmentClass.java.cast(fragment())!!

/**
 * Returns a new [F] associated with [this]
 */
inline fun <D : Any, reified F : Fragment> D.fragmentOrNull(fragmentClass: KClass<F> = F::class) =
    try {
        fragment(fragmentClass)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }