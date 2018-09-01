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

package com.ivianuu.compass.extension

import com.google.common.base.CaseFormat
import com.ivianuu.compass.util.CLASS_BUNDLE
import com.ivianuu.compass.util.TargetType
import com.squareup.kotlinpoet.*

/**
 * @author Manuel Wrage (IVIanuu)
 */
class ExtensionGenerator(private val descriptor: ExtensionDescriptor) {

    fun generate(): FileSpec {
        val file = FileSpec.builder(descriptor.packageName, descriptor.fileName)
        file.addFunction(destinationToBundle())
        file.addFunction(tryBundleAsDestination())
        file.addFunction(bundleAsDestination())
        targetGetDestination()?.let(file::addFunction)
        targetBindDestination()?.let(file::addFunction)
        return file.build()
    }

    private fun destinationToBundle(): FunSpec {
        return FunSpec.builder("toBundle")
            .receiver(descriptor.destination)
            .returns(CLASS_BUNDLE)
            .addCode(
                CodeBlock.builder()
                    .addStatement("return %T.toBundle(this)", descriptor.serializer)
                    .build()
            )
            .build()
    }

    private fun tryBundleAsDestination(): FunSpec {
        return FunSpec.builder("tryAs${descriptor.destination.simpleName()}")
            .receiver(CLASS_BUNDLE.asNullable())
            .returns(descriptor.destination.asNullable())
            .beginControlFlow("return try")
            .addStatement("%T.fromBundle(this!!)", descriptor.serializer)
            .endControlFlow()
            .beginControlFlow("catch(e: Throwable)")
            .addStatement("null")
            .endControlFlow()
            .build()
    }

    private fun bundleAsDestination(): FunSpec {
        return FunSpec.builder("as${descriptor.destination.simpleName()}")
            .receiver(ClassName("android.os", "Bundle").asNullable())
            .returns(descriptor.destination)
            .beginControlFlow("return try")
            .addStatement("%T.fromBundle(this!!)", descriptor.serializer)
            .endControlFlow()
            .beginControlFlow("catch(e: Throwable)")
            .addStatement("throw IllegalArgumentException(e)")
            .endControlFlow()
            .build()
    }

    private fun targetGetDestination(): FunSpec? {
        val simpleName = descriptor.destination.simpleName()
        val functionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,
            descriptor.destination.simpleName())

        return when(descriptor.targetType) {
            TargetType.ACTIVITY -> {
                FunSpec.builder(functionName)
                    .receiver(descriptor.target)
                    .returns(descriptor.destination)
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("return intent.extras.as$simpleName()")
                            .build()
                    )
                    .build()
            }
            TargetType.FRAGMENT, TargetType.FRAGMENTX -> {
                FunSpec.builder(functionName)
                    .receiver(descriptor.target)
                    .returns(descriptor.destination)
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("return arguments.as$simpleName()")
                            .build()
                    )
                    .build()
            }
            TargetType.UNKNOWN -> null
        }
    }

    private fun targetBindDestination(): FunSpec? {
        val functionName = "bind${descriptor.destination.simpleName()}"
        val getName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,
            descriptor.destination.simpleName())

        return when(descriptor.targetType) {
            TargetType.UNKNOWN -> null
            else -> {
                FunSpec.builder(functionName)
                    .receiver(descriptor.target)
                    .returns(
                        ParameterizedTypeName.get(
                            Lazy::class.asClassName(),
                            descriptor.destination
                        )
                    )
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("return lazy(LazyThreadSafetyMode.NONE) { $getName() }")
                            .build()
                    )
                    .build()
            }
        }
    }
}