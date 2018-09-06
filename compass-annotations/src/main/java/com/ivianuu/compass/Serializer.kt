package com.ivianuu.compass

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Serializer(val clazz: KClass<*>)