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

import android.content.Context
import android.content.Intent
import com.ivianuu.compass.CompassRouteFactory
import com.ivianuu.compass.routeFactory
import com.ivianuu.compass.serializerOrNull
import kotlin.reflect.KClass

/**
 * Creates [Intent] from [this]
 */
interface ActivityRouteFactory<T : Any> : CompassRouteFactory {
    /**
     * Returns a new [Intent] associated with [T]
     */
    fun createActivityIntent(context: Context, destination: T): Intent
}

/**
 * Returns a new [ActivityRouteFactory] associated with the [destinationClass] or throws
 */
fun <D : Any> activityRouteFactory(destinationClass: KClass<out D>) =
    routeFactory<ActivityRouteFactory<D>>(destinationClass)

/**
 * Returns a new [ActivityRouteFactory] associated with [this] or throws
 */
fun <T : Any> T.activityRouteFactory() = activityRouteFactory(this::class)

/**
 * Returns a new [ActivityRouteFactory] associated with the [destinationClass] or null
 */
fun <D : Any> activityRouteFactoryOrNull(destinationClass: KClass<out D>) = try {
    activityRouteFactory(destinationClass)
} catch (e: Exception) {
    null
}

/**
 * Returns a new [ActivityRouteFactory] associated with [this] or null
 */
fun <T : Any> T.activityRouteFactoryOrNull() = activityRouteFactoryOrNull(this::class)

/**
 * Returns a new [Intent] associated with [this] or throws
 */
fun <D : Any> D.intent(context: Context): Intent {
    val routeFactory = activityRouteFactory()
    val intent = routeFactory.createActivityIntent(context, this)
    serializerOrNull()?.toBundle(this)?.let { intent.putExtras(it) }
    return intent
}

/**
 * Returns a new [Intent] associated with [this] or throws
 */
fun <D : Any> D.intentOrNull(context: Context) = try {
    intent(context)
} catch (e: Exception) {
    null
}