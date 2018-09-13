@file:Suppress("UNCHECKED_CAST")

package com.ivianuu.compass

import java.lang.reflect.Method
import kotlin.reflect.KClass

private const val SUFFIX_SERIALIZER_PROVIDER = "__SerializerProvider"
private val serializerMethods = mutableMapOf<Class<*>, Method>()

fun <D : Any> serializer(destinationClass: KClass<out D>): CompassSerializer<D> {
    val serializerProviderClass = findClazz(
        destinationClass.java.name.replace("\$", "_") + SUFFIX_SERIALIZER_PROVIDER,
        destinationClass.java.classLoader
    )!!

    return findMethod(serializerProviderClass, METHOD_NAME_GET, serializerMethods)!!
        .invoke(null) as CompassSerializer<D>
}

fun <D : Any> D.serializer() = serializer(this::class)

fun <D : Any> serializerOrNull(destinationClass: KClass<out D>) = try {
    serializer(destinationClass)
} catch (e: Exception) {
    null
}

fun <D : Any> D.serializerOrNull() = serializerOrNull(this::class)