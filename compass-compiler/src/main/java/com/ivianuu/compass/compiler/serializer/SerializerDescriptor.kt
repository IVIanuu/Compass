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

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

data class SerializerDescriptor(
    val element: TypeElement,
    val packageName: String,
    val destination: ClassName,
    val serializer: ClassName,
    val isKotlinObject: Boolean,
    val attributes: Set<SerializerAttribute>,
    val keys: Set<SerializerAttributeKey>
)

data class SerializerAttribute(
    val element: VariableElement,
    val name: String,
    val keyName: String,
    val descriptor: SerializerAttributeDescriptor,
    val isNullable: Boolean,
    val hasDefaultParameter: Boolean
)

data class SerializerAttributeKey(
    val name: String,
    val value: String
)