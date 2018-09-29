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

package com.ivianuu.compass.director

import com.ivianuu.director.Controller
import com.ivianuu.director.Router
import com.ivianuu.director.RouterTransaction
import com.ivianuu.traveler.Command
import com.ivianuu.traveler.Forward
import com.ivianuu.traveler.Replace
import com.ivianuu.traveler.director.ControllerNavigator

/**
 * A [ControllerNavigator] which uses compass
 */
open class CompassControllerNavigator(
    router: Router
) : ControllerNavigator(router) {

    override fun createController(key: Any, data: Any?): Controller? {
        if (key is CompassControllerKey) return key.createController(data)
        return key.controllerOrNull() ?: super.createController(key, data)
    }

    override fun setupTransaction(
        command: Command,
        currentController: Controller?,
        nextController: Controller,
        transaction: RouterTransaction
    ) {
        val key = when (command) {
            is Forward -> command.key
            is Replace -> command.key
            else -> null
        }

        val data = when (command) {
            is Forward -> command.data
            is Replace -> command.data
            else -> null
        }

        if (key is CompassControllerKey) {
            key.setupTransaction(command, currentController, nextController, transaction)
        } else if (key != null) {
            key.controllerDetourOrNull()?.setupTransaction(
                key, data,
                currentController, nextController, transaction
            )
        }
    }
}