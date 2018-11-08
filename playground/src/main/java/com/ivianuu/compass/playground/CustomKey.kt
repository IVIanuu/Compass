package com.ivianuu.compass.playground

import com.ivianuu.compass.Key

/**
 * @author Manuel Wrage (IVIanuu)
 */
data class CustomKeyDestination(
    @Key("my_key") val customKey: String
)