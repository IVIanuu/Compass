@file:Suppress("UNCHECKED_CAST")

package com.ivianuu.compass

import java.lang.reflect.Method
import kotlin.reflect.KClass

private const val SUFFIX_DETOUR_PROVIDER = "__DetourProvider"
private val detourMethods = mutableMapOf<Class<*>, Method>()

@JvmName("detourTyped")
fun <T : CompassDetour> detour(destinationClass: KClass<*>): T {
    val detourProviderClass = findClazz(
        destinationClass.java.name.replace("\$", "_") + SUFFIX_DETOUR_PROVIDER,
        destinationClass.java.classLoader
    )!!

    return findMethod(detourProviderClass, METHOD_NAME_GET, detourMethods)!!
        .invoke(null) as T
}

fun <T : CompassDetour> Any.detour() = detour<T>(this::class)

fun <T : CompassDetour> detourOrNull(destinationClass: KClass<*>) = try {
    detour<T>(destinationClass)
} catch (e: Exception) {
    null
}

fun <T : CompassDetour> Any.detourOrNull() = detourOrNull<T>(this::class)

fun <D : Any> activityDetour(destinationClass: KClass<out D>) =
    detour<ActivityDetour<D>>(destinationClass)

fun <D : Any> D.activityDetour() = activityDetour(this::class)

fun <D : Any> activityDetourOrNull(destinationClass: KClass<out D>) = try {
    activityDetour(destinationClass)
} catch (e: Exception) {
    null
}

fun <D : Any> D.activityDetourOrNull() = activityDetourOrNull(this::class)

fun <D : Any> fragmentDetour(destinationClass: KClass<out D>) =
    detour<FragmentDetour<D>>(destinationClass)

fun <D : Any> D.fragmentDetour() = fragmentDetour(this::class)

fun <D : Any> fragmentDetourOrNull(destinationClass: KClass<out D>) = try {
    fragmentDetour(destinationClass)
} catch (e: Exception) {
    null
}

fun <D : Any> D.fragmentDetourOrNull() = fragmentDetourOrNull(this::class)