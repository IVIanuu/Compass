package com.ivianuu.compass.playground

import com.ivianuu.compass.Key
import com.ivianuu.compass.Serialize

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Serialize
data class CustomKeyDestination(
    @Key("my_key") val customKey: String
)