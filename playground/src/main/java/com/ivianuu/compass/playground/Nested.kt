package com.ivianuu.compass.playground

import com.ivianuu.compass.Destination


// todo support this

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DummyFragment::class)
data class OuterDestination(val value: String) {
    @Destination(DummyFragment::class)
    data class InnerDestination(val value: String)
}