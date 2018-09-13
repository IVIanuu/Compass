package com.ivianuu.compass

import java.lang.reflect.Method

internal const val METHOD_NAME_GET = "get"

private val unexistingClasses = mutableSetOf<String>()

internal fun findClazz(
    className: String,
    classLoader: ClassLoader?
): Class<*>? {
    if (unexistingClasses.contains(className)) return null

    return try {
        classLoader?.loadClass(className)
    } catch (e: Exception) {
        unexistingClasses.add(className)
        null
    }
}

internal fun findMethod(
    clazz: Class<*>,
    methodName: String,
    map: MutableMap<Class<*>, Method>
): Method? {
    var method = map[clazz]
    if (method != null) {
        return method
    }

    return try {
        method = clazz.declaredMethods.firstOrNull { it.name == methodName }
        if (method != null) {
            map[clazz] = method
        }
        method
    } catch (e: Exception) {
        null
    }
}