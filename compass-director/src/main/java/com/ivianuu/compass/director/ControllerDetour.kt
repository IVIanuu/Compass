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

import com.ivianuu.compass.CompassDetour
import com.ivianuu.compass.detour
import com.ivianuu.director.Controller
import com.ivianuu.director.RouterTransaction
import kotlin.reflect.KClass

/**
 * Applies change handlers to a [RouterTransaction]
 */
interface ControllerDetour<T : Any> : CompassDetour {
    /**
     * Setup the [transaction] to apply transitions
     */
    fun setupTransaction(
        destination: T,
        data: Any?,
        currentController: Controller?,
        nextController: Controller,
        transaction: RouterTransaction
    )
}

/**
 * Returns a new [ControllerDetour] associated with the [destinationClass] or throws
 */
fun <D : Any> controllerDetour(destinationClass: KClass<out D>) =
    detour<ControllerDetour<D>>(destinationClass)

/**
 * Returns a new [ControllerDetour] associated with [this] or throws
 */
fun <D : Any> D.controllerDetour() = controllerDetour(this::class)

/**
 * Returns a new [ControllerDetour] associated with the [destinationClass] or null
 */
fun <D : Any> controllerDetourOrNull(destinationClass: KClass<out D>) = try {
    controllerDetour(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [ControllerDetour] associated with [this] or null
 */
fun <D : Any> D.controllerDetourOrNull() = controllerDetourOrNull(this::class)