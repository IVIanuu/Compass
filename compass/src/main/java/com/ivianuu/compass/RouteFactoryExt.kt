package com.ivianuu.compass

import java.lang.reflect.Method
import kotlin.reflect.KClass

private const val SUFFIX_ROUTE_PROVIDER = "__RouteProvider"
private val routeMethods = mutableMapOf<Class<*>, Method>()

fun <T : CompassRouteFactory> routeFactory(destinationClass: KClass<*>): T {
    val routeProviderClass = findClazz(
        destinationClass.java.name.replace("\$", "_") + SUFFIX_ROUTE_PROVIDER,
        destinationClass.java.classLoader
    )!!

    return findMethod(routeProviderClass, METHOD_NAME_GET, routeMethods)!!
        .invoke(null) as T
}

fun <T : CompassRouteFactory> Any.routeFactory() = routeFactory<T>(this::class)

fun <T : CompassRouteFactory> routeFactoryOrNull(destinationClass: KClass<*>) = try {
    routeFactory<T>(destinationClass)
} catch (e: Exception) {
    null
}

fun <T : CompassRouteFactory> Any.routeFactoryOrNull() = routeFactoryOrNull<T>(this::class)

fun <D : Any> activityRouteFactory(destinationClass: KClass<out D>) =
    routeFactory<ActivityRouteFactory<D>>(destinationClass)

fun <T : Any> T.activityRouteFactory() = activityRouteFactory(this::class)

fun <D : Any> activityRouteFactoryOrNull(destinationClass: KClass<out D>) = try {
    activityRouteFactory(destinationClass)
} catch (e: Exception) {
    null
}

fun <T : Any> T.activityRouteFactoryOrNull() = activityRouteFactoryOrNull(this::class)

fun <D : Any> fragmentRouteFactory(destinationClass: KClass<out D>) =
    routeFactory<FragmentRouteFactory<D>>(destinationClass)

fun <T : Any> T.fragmentRouteFactory() = fragmentRouteFactory(this::class)

fun <D : Any> fragmentRouteFactoryOrNull(destinationClass: KClass<out D>) = try {
    fragmentRouteFactory(destinationClass)
} catch (e: Exception) {
    null
}

fun <T : Any> T.fragmentRouteFactoryOrNull() = fragmentRouteFactoryOrNull(this::class)