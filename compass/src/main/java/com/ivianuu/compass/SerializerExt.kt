@file:Suppress("UNCHECKED_CAST")

package com.ivianuu.compass

import java.lang.reflect.Method
import kotlin.reflect.KClass

private const val SUFFIX_SERIALIZER_PROVIDER = "__SerializerProvider"
private val serializerMethods = mutableMapOf<Class<*>, Method>()

/**
 * Returns a new [CompassSerializer] associated with the [destinationClass] or throws
 */
fun <D : Any> serializer(destinationClass: KClass<out D>): CompassSerializer<D> {
    val serializerProviderClass = findClazz(
        destinationClass.java.name.replace("\$", "_") + SUFFIX_SERIALIZER_PROVIDER,
        destinationClass.java.classLoader
    )!!

    return findMethod(serializerProviderClass, METHOD_NAME_GET, serializerMethods)!!
        .invoke(null) as CompassSerializer<D>
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
    null
}

/**
 * Returns a new [CompassSerializer] associated with [this] or null
 */
fun <D : Any> D.serializerOrNull() = serializerOrNull(this::class)