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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.ivianuu.traveler.commands.Command
import com.ivianuu.traveler.commands.Forward
import com.ivianuu.traveler.commands.Replace
import com.ivianuu.traveler.fragments.FragmentAppNavigator

/**
 * Compass navigator
 */
@Suppress("UNCHECKED_CAST")
open class CompassNavigator(
    activity: Activity,
    fragmentManager: FragmentManager,
    containerId: Int
) : FragmentAppNavigator(activity, fragmentManager, containerId) {

    override fun createFragment(key: Any, data: Any?): Fragment? {
        return Compass.getRouteFactory<FragmentRouteFactory<Any>>(key)
            ?.createFragment(key)?.apply {
                val args = arguments ?: Bundle().also { arguments = it }
                Compass.toBundle(key, args)
            }
    }

    override fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        transaction: FragmentTransaction
    ) {
        val destination = when(command) {
            is Replace -> command.key
            is Forward -> command.key
            else -> throw IllegalArgumentException()
        }

        val detour =
            Compass.getDetour<FragmentDetour<Any>>(destination)
        detour?.setup(destination, currentFragment, nextFragment, transaction)
    }

    override fun createActivityIntent(context: Context, key: Any, data: Any?): Intent? {
        return Compass.getRouteFactory<ActivityRouteFactory<Any>>(key)
            ?.createIntent(context, key)?.apply {
                extras.putAll(Compass.toBundle(key))
            }
    }

    override fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
        val destination = when(command) {
            is Replace -> command.key
            is Forward -> command.key
            else -> throw IllegalArgumentException() // this should never happen
        }

        val detour =
            Compass.getDetour<ActivityDetour<Any>>(destination)

        return detour?.createOptions(destination, activityIntent)
    }
}