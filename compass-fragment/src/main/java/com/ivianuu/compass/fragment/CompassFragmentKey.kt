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
import com.ivianuu.traveler.fragment.FragmentKey

/**
 * A [ControllerKey] which creates a [Controller] via compass
 */
interface CompassFragmentKey : FragmentKey {
    override fun createFragment(data: Any?) = fragment()
    override fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    ) {
        val data = when (command) {
            is Forward -> command.data
            is Replace -> command.data
            else -> null
        }

        fragmentDetourOrNull()?.setupTransaction(
            this, data,
            currentFragment, nextFragment, transaction
        )
    }
}