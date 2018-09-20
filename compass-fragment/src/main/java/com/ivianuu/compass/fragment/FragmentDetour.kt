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

package com.ivianuu.compass.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ivianuu.compass.CompassDetour
import com.ivianuu.compass.detour
import kotlin.reflect.KClass

/**
 * Applies transitions to a [Fragment] or a [FragmentTransaction]
 */
interface FragmentDetour<T : Any> : CompassDetour {

    /**
     * Setup the [transaction] to apply transitions
     */
    fun setupTransaction(
        destination: T,
        data: Any?,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    )
}

/**
 * Returns a new [FragmentDetour] associated with the [destinationClass] or throws
 */
fun <D : Any> fragmentDetour(destinationClass: KClass<out D>) =
    detour<FragmentDetour<D>>(destinationClass)

/**
 * Returns a new [FragmentDetour] associated with [this] or throws
 */
fun <D : Any> D.fragmentDetour() = fragmentDetour(this::class)

/**
 * Returns a new [FragmentDetour] associated with the [destinationClass] or null
 */
fun <D : Any> fragmentDetourOrNull(destinationClass: KClass<out D>) = try {
    fragmentDetour(destinationClass)
} catch (e: Exception) {
    null
}

/**
 * Returns a new [FragmentDetour] associated with [this] or null
 */
fun <D : Any> D.fragmentDetourOrNull() = fragmentDetourOrNull(this::class)