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
import com.ivianuu.traveler.director.ControllerNavigator

/**
 * A [ControllerNavigator] which uses compass
 */
open class CompassControllerNavigator(
    router: Router
) : ControllerNavigator(router) {

    private val controllerNavigatorHelper = CompassControllerNavigatorHelper()

    override fun createController(key: Any, data: Any?): Controller? =
        controllerNavigatorHelper.createController(key, data)

    override fun setupTransaction(command: Command, transaction: RouterTransaction) {
        controllerNavigatorHelper.setupTransaction(command, transaction)
    }
}