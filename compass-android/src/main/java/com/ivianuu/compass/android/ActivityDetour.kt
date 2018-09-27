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

package com.ivianuu.compass.android

import android.content.Intent
import android.os.Bundle
import com.ivianuu.compass.CompassDetour
import com.ivianuu.compass.detour
import kotlin.reflect.KClass

/**
 * Creates activity start options for animating between screens
 */
interface ActivityDetour<T : Any> : CompassDetour {
    /**
     * Returns activity start options which will be passed with the [intent]
     */
    fun createStartActivityOptions(destination: T, data: Any?, intent: Intent): Bundle?
}

/**
 * Returns a new [ActivityDetour] associated with [this] or throws
 */
fun <D : Any> activityDetour(destinationClass: KClass<out D>) =
    detour<ActivityDetour<D>>(destinationClass)

/**
 * Returns a new [ActivityDetour] associated with [this] or throws
 */
fun <D : Any> D.activityDetour() = activityDetour(this::class)

/**
 * Returns a new [ActivityDetour] associated with the [destinationClass] or null
 */
fun <D : Any> activityDetourOrNull(destinationClass: KClass<out D>) = try {
    activityDetour(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [ActivityDetour] associated with [this] or null
 */
fun <D : Any> D.activityDetourOrNull() = activityDetourOrNull(this::class)