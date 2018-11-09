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

package com.ivianuu.compass.sample

import android.os.Bundle
import android.util.Log
import com.ivianuu.compass.CompassSerializer
import com.ivianuu.compass.Destination
import com.ivianuu.compass.RouteFactory
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

fun Bundle.putAuto(key: String, value: Any) {
    when (value) {
        is ArrayList<*> -> {

        }
        is List<*> -> {

        }
    }
}

class ReflectSerializer<T : Any>(private val destinationClass: KClass<T>) : CompassSerializer<T> {

    init {
        checkIfNecessary()
    }

    override fun toBundle(destination: T, bundle: Bundle) {
        val destinationInfo = getInfo()

        destinationInfo.properties.forEach { propInfo ->
            val value = propInfo.property.get(destination)
            bundle.putAuto(propInfo.keyName, value)
        }
    }

    override fun fromBundle(bundle: Bundle): T {
        val destinationInfo = getInfo()
        val paramsWithValues = destinationInfo.properties
            .map { it to bundle.get(it.keyName) }

        // check if any required value is present
        val allPresent = paramsWithValues
            .filter { !it.first.isNullable }
            .all { it.second != null }

        if (!allPresent) {
            throw IllegalArgumentException("corrupt bundle")
        }

        return destinationInfo.konstructor.call(*paramsWithValues.map { it.second }.toTypedArray()) as T
    }

    private fun getInfo(): DestinationInfo {
        return destinationInfos.getOrPut(destinationClass) {
            DestinationInfo(destinationClass.primaryConstructor!!,
                destinationClass.primaryConstructor!!.parameters.map { param ->
                    PropertyInfo(
                        destinationClass.memberProperties.first { it.name == param.name } as KProperty1<Any, Any>,
                        destinationClass.java.name + "_" + param.name,
                        param.type.isMarkedNullable
                    )
                }
                    .toSet()
            )
        }
    }

    private fun checkIfNecessary() {
        // do not check twice
        if (checkedDestinations.contains(destinationClass)) return

        val klass = destinationClass
        val javaKlass = klass.java

        val destinationAnnotation = javaKlass.getAnnotation(Destination::class.java)
            ?: throw IllegalArgumentException("missing @Destination annotation.")

        // check if a route factory was specified
        if (destinationAnnotation.target == Nothing::class) {
            if (javaKlass.getAnnotation(RouteFactory::class.java) == null) {
                throw IllegalArgumentException("either a target or a @RouteFactory must be specified.")
            }
        }

        // get the primary constructor
        val primaryConstructor = klass.primaryConstructor
            ?: throw IllegalArgumentException("destinations must have a primary constructor")

        val constructorParams = primaryConstructor.parameters

        val onlyProperties = constructorParams.all { param ->
            klass.memberProperties.any { it.name == param.name }
        }

        if (!onlyProperties) {
            throw IllegalArgumentException("the primary constructor can only have properties")
        }

        constructorParams.forEach { param ->
            val javaClass = param.type.jvmErasure.java
            Log.d("testt", "java class for param ${param.name} is $javaClass")
        }

        // todo check if each type is allowed in bundles
    }

    private fun isSupportedType(clazz: Class<*>) = when (clazz) {
        Boolean::class.java -> true
        Byte::class.java -> true
        else -> false
    }

    private data class DestinationInfo(
        val konstructor: KCallable<Any>,
        val properties: Set<PropertyInfo>
    )

    private data class PropertyInfo(
        val property: KProperty1<Any, Any>,
        val keyName: String,
        val isNullable: Boolean
    )

    private companion object {
        private val checkedDestinations = mutableSetOf<KClass<*>>()
        private val destinationInfos = mutableMapOf<KClass<*>, DestinationInfo>()
    }
}