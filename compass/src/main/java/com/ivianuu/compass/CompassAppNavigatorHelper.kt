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
import android.os.Bundle
import com.ivianuu.traveler.commands.Command
import com.ivianuu.traveler.commands.Forward
import com.ivianuu.traveler.commands.Replace

/**
 * @author Manuel Wrage (IVIanuu)
 */
class CompassAppNavigatorHelper {

    fun createActivityIntent(context: Context, key: Any, data: Any?): Intent? {
        return Compass.getRouteFactory<ActivityRouteFactory<Any>>(key)
            ?.createIntent(context, key)?.apply {
                val serializer = Compass.getSerializer<Any>(key)
                serializer?.toBundle(key)?.let(this::putExtras)
            }
    }

    fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
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