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
import com.ivianuu.director.RouterTransaction
import com.ivianuu.traveler.Command
import com.ivianuu.traveler.Forward
import com.ivianuu.traveler.Replace

/**
 * Helper class for implementing an [ControllerNavigator] via compass
 */
class CompassControllerNavigatorHelper {

    /**
     * Returns a matching [Controller] or null
     */
    fun createController(key: Any, data: Any?): Controller? = key.controllerOrNull()

    /**
     * Setups the router transaction if a matching [ControllerDetour] was found
     */
    fun setupTransaction(
        command: Command,
        transaction: RouterTransaction
    ) {
        val (destination, data) = when (command) {
            is Replace -> command.key to command.data
            is Forward -> command.key to command.data
            else -> throw IllegalArgumentException()
        }

        destination.controllerDetourOrNull()
            ?.setupTransaction(destination, data, transaction)
    }
}