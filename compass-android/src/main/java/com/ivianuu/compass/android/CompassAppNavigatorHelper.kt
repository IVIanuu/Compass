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
import android.os.Bundle
import com.ivianuu.traveler.Command
import com.ivianuu.traveler.Forward
import com.ivianuu.traveler.Replace

/**
 * Helper class for implementing an [AppNavigator] via compass
 */
class CompassAppNavigatorHelper {

    /**
     * Returns a matching [Intent] or null
     */
    fun createActivityIntent(context: Context, key: Any, data: Any?) = key.intentOrNull(context)

    /**
     * Returns matching activity start options or null
     */
    fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
        val (destination, data) = when (command) {
            is Replace -> command.key to command.data
            is Forward -> command.key to command.data
            else -> throw IllegalArgumentException() // this should never happen
        }

        return destination.activityDetourOrNull()
            ?.createStartActivityOptions(destination, data, activityIntent)
    }
}