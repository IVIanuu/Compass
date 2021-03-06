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

package com.ivianuu.compass.compiler.serializer

import com.ivianuu.compass.compiler.util.CLASS_BUNDLE
import com.ivianuu.compass.compiler.util.CLASS_SERIALIZER
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName

class SerializerGenerator(private val descriptor: SerializerDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName,
            descriptor.serializer.simpleName())

        // import
        descriptor.attributes
            .flatMap {
                listOf(
                    it.descriptor.getFunction,
                    it.descriptor.getOrThrowFunction,
                    it.descriptor.putFunction
                )
            }
            .filter { it.import }
            .forEach { file.addStaticImport("com.ivianuu.compass", it.name) }

        val type = TypeSpec.classBuilder(descriptor.serializer)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_SERIALIZER,
                    descriptor.destination
                )
            )

        type.addFunction(toBundle())
        type.addFunction(fromBundle())
        type.addType(companionObject())

        file.addType(type.build())

        return file.build()
    }

    private fun companionObject(): TypeSpec =
        TypeSpec.companionObjectBuilder()
            .addProperties(keyProperties())
            .build()

    private fun keyProperties(): Set<PropertySpec> {
        return descriptor.keys
            .map {
                PropertySpec.builder(it.name, String::class.asTypeName(), KModifier.CONST)
                    .initializer("\"${it.value}\"")
                    .build()
            }
            .toSet()
    }

    private fun toBundle(): FunSpec {
        val function = FunSpec.builder("toBundle")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", descriptor.destination)
            .addParameter("bundle", CLASS_BUNDLE)

        descriptor.attributes.forEach { function.addBundlePutter(it) }

        return function.build()
    }

    private fun fromBundle(): FunSpec {
        val function = FunSpec.builder("fromBundle")
            .addModifiers(KModifier.OVERRIDE)
            .returns(descriptor.destination)
            .addParameter("bundle", CLASS_BUNDLE)

        descriptor.attributes.forEach { function.addBundleGetter(it) }

        val constructorStatement = "return %T(${
        descriptor.attributes.joinToString(", ") { it.name }})"

        function.addStatement(constructorStatement, descriptor.destination)

        return function.build()
    }

    private fun FunSpec.Builder.addBundlePutter(attribute: SerializerAttribute) {
        if (attribute.isNullable) {
            beginControlFlow("if (destination.${attribute.name} != null)")
        }

        addStatement(
            "bundle.${attribute.descriptor.putFunction.name}(${attribute.keyName}, destination.${attribute.name})"
        )
        if (attribute.isNullable) {
            endControlFlow()
        }
    }

    private fun FunSpec.Builder.addBundleGetter(attribute: SerializerAttribute) {
        val descriptor = attribute.descriptor
        val function =
            if (attribute.isNullable) descriptor.getFunction else descriptor.getOrThrowFunction

        val typeParameter = function.typeParameter

        if (typeParameter != null) {
            addStatement(
                "val ${attribute.name} = bundle.${function.name}<%T>(${attribute.keyName})",
                typeParameter
            )
        } else {
            addStatement(
                "val ${attribute.name} = bundle.${function.name}(${attribute.keyName})"
            )
        }
    }
}