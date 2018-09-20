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

package com.ivianuu.compass.compiler.extension

import com.ivianuu.compass.compiler.util.TargetType
import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement

data class ExtensionDescriptor(
    val element: TypeElement,
    val packageName: String,
    val destination: ClassName,
    val target: ClassName,
    val targetType: TargetType,
    val fileName: String,
    val serializer: ClassName
)