package com.ivianuu.compass.sample

import com.ivianuu.compass.Destination


// todo support this

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination
data class OuterDestination(val value: String) {
    @Destination
    data class InnerDestination(val value: String)
}