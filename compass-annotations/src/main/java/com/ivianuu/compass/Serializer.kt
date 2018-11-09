package com.ivianuu.compass

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Serializer(val clazz: KClass<*>)