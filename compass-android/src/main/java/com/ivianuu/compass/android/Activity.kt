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

package com.ivianuu.compass.android

import android.app.Activity
import com.ivianuu.compass.asDestination
import kotlin.reflect.KClass

/**
 * Binds [D] from [Activity.intent]
 */
fun <D : Any> Activity.bindDestination(destinationClass: KClass<D>) =
    lazy(LazyThreadSafetyMode.NONE) { destination(destinationClass) }

/**
 * Binds [D] from [Activity.intent]
 */
inline fun <reified D : Any> Activity.bindDestination() =
    bindDestination(D::class)

/**
 * Returns [D] from [Activity.intent]
 */
fun <D : Any> Activity.destination(destinationClass: KClass<D>) =
    intent.extras.asDestination(destinationClass)

/**
 * Returns [D] from [Activity.intent]
 */
inline fun <reified D : Any> Activity.destination() = destination(D::class)