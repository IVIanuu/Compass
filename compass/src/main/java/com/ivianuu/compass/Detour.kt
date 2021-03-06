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

import kotlin.reflect.KClass

/**
 * Applies transitions
 */
interface CompassDetour

private val detourClasses = mutableMapOf<Class<*>, Class<*>>()

/**
 * Returns a new [CompassDetour] associated with the [destinationClass] or throws
 */
@JvmName("detourTyped")
fun <T : CompassDetour> detour(destinationClass: KClass<*>): T {
    val detourClass = detourClasses.getOrPut(destinationClass.java) {
        val detourAnnotation = destinationClass.java.getAnnotation(Detour::class.java)
            ?: throw IllegalArgumentException("missing @Detour annotation $destinationClass")
        detourAnnotation.clazz.java
    }

    return detourClass.newInstance() as T
}

/**
 * Returns a new [CompassDetour] associated with [this] or throws
 */
fun <T : CompassDetour> Any.detour() = detour<T>(this::class)

/**
 * Returns a new [CompassDetour] associated with the [destinationClass] or null
 */
fun <T : CompassDetour> detourOrNull(destinationClass: KClass<*>) = try {
    detour<T>(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [CompassDetour] associated with [this] or null
 */
fun <T : CompassDetour> Any.detourOrNull() = detourOrNull<T>(this::class)