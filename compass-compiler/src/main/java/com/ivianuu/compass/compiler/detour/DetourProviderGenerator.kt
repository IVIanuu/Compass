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

package com.ivianuu.compass.compiler.detour

import com.ivianuu.compass.compiler.util.CLASS_DETOUR
import com.ivianuu.compass.compiler.util.CLASS_DETOUR_PROVIDER
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class DetourProviderGenerator(private val descriptor: DetourProviderDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName,
            descriptor.detourProvider.simpleName())

        val type = TypeSpec.objectBuilder(descriptor.detourProvider)
            .addSuperinterface(CLASS_DETOUR_PROVIDER)
            .addFunction(get())

        file.addType(type.build())

        return file.build()
    }

    private fun get(): FunSpec {
        return FunSpec.builder("get")
            .addAnnotation(JvmStatic::class.java)
            .returns(CLASS_DETOUR)
            .addCode(
                CodeBlock.builder()
                    .addStatement("return %T()", descriptor.detour)
                    .build()
            )
            .build()
    }

}