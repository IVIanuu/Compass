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

package com.ivianuu.compass.serializer

import com.ivianuu.compass.util.CLASS_BUNDLE
import com.ivianuu.compass.util.CLASS_SERIALIZER
import com.squareup.kotlinpoet.*

/**
 * @author Manuel Wrage (IVIanuu)
 */
class SerializerGenerator(private val descriptor: SerializerDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName,
            descriptor.serializer.simpleName())

        val type = TypeSpec.objectBuilder(descriptor.serializer)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    CLASS_SERIALIZER,
                    descriptor.destination
                )
            )

        type.addProperties(keyProperties())
        type.addFunction(toBundle())
        type.addFunction(fromBundle())

        file.addType(type.build())

        return file.build()
    }

    private fun keyProperties(): Set<PropertySpec> {
        return descriptor.keys
            .map {
                PropertySpec.builder(it.name, String::class.asTypeName(), KModifier.PRIVATE, KModifier.CONST)
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

        if (descriptor.attributes.isNotEmpty() || !descriptor.isKotlinObject) {
            descriptor.attributes.forEach { function.addBundleGetter(it) }

            val constructorStatement = "return %T(${
            descriptor.attributes
                .map(DestinationAttribute::name)
                .joinToString(", ")
            })"

            function.addStatement(constructorStatement,descriptor.destination)
        } else {
            function.addStatement("return %T", descriptor.destination)
        }

        return function.build()
    }

    private fun FunSpec.Builder.addBundlePutter(attribute: DestinationAttribute) {
        if (attribute.isNullable) {
            beginControlFlow("if (destination.${attribute.name} != null)")
        }
        addStatement(
            "bundle.put${attribute.descriptor.typeMapping}(${attribute.keyName}, " +
                    (if (attribute.descriptor.wrapInArrayList) "ArrayList(destination.${attribute.name}))" else "destination.${attribute.name})")
        )
        if (attribute.isNullable) {
            endControlFlow()
        }
    }

    private fun FunSpec.Builder.addBundleGetter(attribute: DestinationAttribute) {
        if (attribute.isNullable) {
            // clean this up
            addStatement(
                "val ${attribute.name} = if (bundle.containsKey(${attribute.keyName})) {\n" +
                        "bundle.get${attribute.descriptor.typeMapping}" +
                        (if (attribute.descriptor.typeParameter != null) "<${attribute.descriptor.typeParameter}>" else "") +
                        "(${attribute.keyName}) " +
                        (if (attribute.descriptor.castTo != null) "as ${attribute.descriptor.castTo}" else "") +
                        "\n} else {\nnull\n}"
            )
        } else {
            addStatement(
                "val ${attribute.name} = " +
                        "bundle.get${attribute.descriptor.typeMapping}" +
                        (if (attribute.descriptor.typeParameter != null) "<${attribute.descriptor.typeParameter}>" else "") +
                        "(${attribute.keyName}) " +
                        (if (attribute.descriptor.castTo != null) "as ${attribute.descriptor.castTo}" else "")
            )
        }
    }
}