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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

/**
 * @author Manuel Wrage (IVIanuu)
 */
object SerializerProviderBuilder {

    fun buildSerializerProvider(
        environment: ProcessingEnvironment,
        element: TypeElement
    ): TypeSpec {
        val serializerName = element.asType().toString() + "__Serializer"

        val type = TypeSpec.objectBuilder("${element.simpleName}__SerializerProvider")
            .addSuperinterface(CLASS_SERIALIZER_PROVIDER)

        val getBuilder = FunSpec.builder("get")
            .addAnnotation(JvmStatic::class.java)
            .returns(ClassName.bestGuess(serializerName))
            .addCode(
                CodeBlock.builder()
                    .addStatement("return %T", ClassName.bestGuess(serializerName))
                    .build()
            )

        type.addFunction(getBuilder.build())

        return type.build()
    }

}