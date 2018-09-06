package com.ivianuu.compass.sample

import com.ivianuu.compass.Destination

// todo support this
/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination
data class DefaultParamsDestination(
    val name: String = "",
    val age: Int = 0,
    val score: Float
)