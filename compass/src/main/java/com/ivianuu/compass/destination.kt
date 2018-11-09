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

import android.content.Intent
import android.os.Bundle
import kotlin.reflect.KClass

/**
 * Returns [this] as [D]
 */
fun <D : Any> Bundle?.asDestination(destinationClass: KClass<D>) =
    serializer(destinationClass).fromBundle(this!!)

/**
 * Returns [this] as [D]
 */
inline fun <reified D : Any> Bundle?.asDestination() =
    asDestination(D::class)

/**
 * Returns [this] as [D] or null
 */
fun <D : Any> Bundle?.asDestinationOrNull(destinationClass: KClass<D>) = try {
    asDestination(destinationClass)
} catch (e: Exception) {
    null
}

/**
 * Returns [this] as [D] or null
 */
inline fun <reified D : Any> Bundle?.asDestinationOrNull() =
    asDestinationOrNull(D::class)

/**
 * Returns a [Bundle] of [this]
 */
fun Any.toBundle(): Bundle = serializer().toBundle(this)

/**
 * Adds [this] to the [bundle]
 */
fun Any.addToBundle(bundle: Bundle) {
    bundle.putAll(toBundle())
}

/**
 * Adds [this] to the [intent]
 */
fun Any.addToIntent(intent: Intent) {
    intent.extras.putAll(toBundle())
}