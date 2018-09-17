package com.ivianuu.compass.playground

import com.ivianuu.compass.Destination

// todo support this
/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DummyFragment::class)
data class DefaultParamsDestination(
    val name: String = "",
    val age: Int = 0,
    val score: Float
)