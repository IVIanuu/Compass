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

package com.ivianuu.compass

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Creates route components e.g [Intent] or [Fragment] from a specific destination
 */
interface CompassRouteFactory

/**
 * Creates [Fragment] from Returns a new
 */
interface FragmentRouteFactory<T : Any> : CompassRouteFactory {
    /**
     * Returns a new [Fragment] associated with [this]
     */
    fun createFragment(destination: T): Fragment
}

/**
 * Creates [Intent] from [this]
 */
interface ActivityRouteFactory<T : Any> : CompassRouteFactory {
    /**
     * Returns a new [Intent] associated with [this]
     */
    fun createActivityIntent(context: Context, destination: T): Intent
}

/**
 * Provides [CompassRouteFactory] for a specific destination
 */
interface CompassRouteFactoryProvider