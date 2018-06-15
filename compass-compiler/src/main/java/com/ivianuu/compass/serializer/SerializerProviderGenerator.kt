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

import com.ivianuu.compass.util.CLASS_SERIALIZER_PROVIDER
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class SerializerProviderGenerator(private val descriptor: SerializerProviderDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName, descriptor.serializerProvider.simpleName())

        val type = TypeSpec.objectBuilder(descriptor.serializerProvider)
            .addSuperinterface(CLASS_SERIALIZER_PROVIDER)
            .addFunction(get())

        file.addType(type.build())

        return file.build()
    }

    private fun get(): FunSpec {
        return FunSpec.builder("get")
            .addAnnotation(JvmStatic::class.java)
            .returns(descriptor.serializer)
            .addCode(
                CodeBlock.builder()
                    .addStatement("return %T", descriptor.serializer)
                    .build()
            )
            .build()

    }
}