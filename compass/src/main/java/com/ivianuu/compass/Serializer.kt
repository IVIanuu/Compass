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
import kotlin.reflect.KClass

/**
 * A converter to create [T] from [Bundle] and vice versa
 */
interface CompassSerializer<T : Any> {

    fun fromBundle(bundle: Bundle): T

    fun toBundle(destination: T, bundle: Bundle)

    fun toBundle(destination: T): Bundle {
        val bundle = Bundle()
        toBundle(destination, bundle)
        return bundle
    }
}

private const val SUFFIX_SERIALIZER = "__Serializer"

/**
 * Returns a new [CompassSerializer] associated with the [destinationClass] or throws
 */
fun <D : Any> serializer(destinationClass: KClass<out D>): CompassSerializer<D> {
    val serializerClass = findClazz(
        destinationClass.java.name.replace("\$", "_") + SUFFIX_SERIALIZER,
        destinationClass.java.classLoader
    )!!

    return serializerClass.newInstance() as CompassSerializer<D>
}

/**
 * Returns a new [CompassSerializer] associated with [this]
 */
fun <D : Any> D.serializer() = serializer(this::class)

/**
 * Returns a new [CompassSerializer] associated with the [destinationClass] or null
 */
fun <D : Any> serializerOrNull(destinationClass: KClass<out D>) = try {
    serializer(destinationClass)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * Returns a new [CompassSerializer] associated with [this] or null
 */
fun <D : Any> D.serializerOrNull() = serializerOrNull(this::class)