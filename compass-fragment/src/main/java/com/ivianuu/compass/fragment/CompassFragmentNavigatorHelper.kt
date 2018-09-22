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
import com.ivianuu.traveler.Command
import com.ivianuu.traveler.Forward
import com.ivianuu.traveler.Replace

/**
 * Helper class for implementing an [FragmentNavigator] via compass
 */
class CompassFragmentNavigatorHelper {

    /**
     * Returns a matching [Fragment] or null
     */
    fun createFragment(key: Any, data: Any?): Fragment? = key.fragmentOrNull()

    /**
     * Setups the fragment transaction if a matching [FragmentDetour] was found
     */
    fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    ) {
        val (destination, data) = when (command) {
            is Replace -> command.key to command.data
            is Forward -> command.key to command.data
            else -> throw IllegalArgumentException()
        }

        destination.fragmentDetourOrNull()
            ?.setupTransaction(destination, data, currentFragment, nextFragment, transaction)
    }
}