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

package com.ivianuu.compass.director

import com.ivianuu.compass.CompassRouteFactory
import com.ivianuu.compass.routeFactory
import com.ivianuu.compass.serializerOrNull
import com.ivianuu.director.Controller
import kotlin.reflect.KClass

/**
 * Creates [Controller] from Returns a new
 */
interface ControllerRouteFactory<T : Any> : CompassRouteFactory {
    /**
     * Returns a new [Controller] associated with the [destination]
     */
    fun createController(destination: T): Controller
}

/**
 * Returns a new [ControllerRouteFactory] associated with the [destinationClass] or throws
 */
fun <D : Any> controllerRouteFactory(destinationClass: KClass<out D>) =
    routeFactory<ControllerRouteFactory<D>>(destinationClass)

/**
 * Returns a new [ControllerRouteFactory] associated with [this] or throws
 */
fun <T : Any> T.controllerRouteFactory() = controllerRouteFactory(this::class)

/**
 * Returns a new [ControllerRouteFactory] associated with the [destinationClass] or null
 */
fun <D : Any> controllerRouteFactoryOrNull(destinationClass: KClass<out D>) = try {
    controllerRouteFactory(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [ControllerRouteFactory] associated with [this] or null
 */
fun <T : Any> T.controllerRouteFactoryOrNull() = controllerRouteFactoryOrNull(this::class)

/**
 * Returns a new [Controller] associated with [this]
 */
fun <D : Any> D.controller(): Controller {
    val routeFactory = controllerRouteFactory()
    val controller = routeFactory.createController(this)
    serializerOrNull()?.toBundle(this)?.let { controller.args = it }
    return controller
}

/**
 * Returns a new [Controller] associated with [this] or null
 */
fun <D : Any> D.controllerOrNull() = try {
    controller()
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [C] associated this [this]
 */
inline fun <D : Any, reified C : Controller> D.controller(controllerClass: KClass<C> = C::class) =
    controllerClass.java.cast(controller())!!

/**
 * Returns a new [C] associated with [this]
 */
inline fun <D : Any, reified C : Controller> D.controllerOrNull(controllerClass: KClass<C> = C::class) =
    try {
        controller(controllerClass)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }