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

package com.ivianuu.compass.sample

import android.graphics.Color

/**
 * @author Manuel Wrage (IVIanuu)
 */
object ColorGenerator {

    private val COLORS = arrayOf(
        Color.BLUE, Color.RED, Color.GREEN,
        Color.MAGENTA, Color.CYAN, Color.YELLOW
    )

    private var lastColor = -1

    fun generate(): Int {
        var color = -1

        while (color == -1 || color == lastColor) {
            color = COLORS.toList().shuffled().first()
        }

        return color.also { lastColor = it }
    }

}