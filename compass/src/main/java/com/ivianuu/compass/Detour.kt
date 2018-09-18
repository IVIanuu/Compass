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

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Applies transitions
 */
interface CompassDetour

/**
 * Applies transitions to a [Fragment] or a [FragmentTransaction]
 */
interface FragmentDetour<T : Any> : CompassDetour {

    /**
     * Setup the [transaction] to apply transitions
     */
    fun setupTransaction(
        destination: T,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    )
}

/**
 * Creates activity start options for animating between screens
 */
interface ActivityDetour<T : Any> : CompassDetour {

    /**
     * Returns activity start options which will be passed with the [intent]
     */
    fun createStartActivityOptions(destination : T, intent: Intent): Bundle?
}

/**
 * Provides [CompassDetour]'s
 */
interface CompassDetourProvider