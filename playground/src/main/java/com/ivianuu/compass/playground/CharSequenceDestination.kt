package com.ivianuu.compass.playground

import com.ivianuu.compass.Destination

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DummyFragment::class)
data class CharSequenceDestination(val sequence: CharSequence)