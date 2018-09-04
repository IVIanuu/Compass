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
import com.ivianuu.traveler.android.AppNavigator
import com.ivianuu.traveler.command.Command

/**
 * @author Manuel Wrage (IVIanuu)
 */
open class CompassAppNavigator(activity: Activity) : AppNavigator(activity) {

    private val appNavigatorHelper = CompassAppNavigatorHelper()

    override fun createActivityIntent(context: Context, key: Any, data: Any?): Intent? =
        appNavigatorHelper.createActivityIntent(context, key, data)

    override fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? =
            appNavigatorHelper.createStartActivityOptions(command, activityIntent)
}