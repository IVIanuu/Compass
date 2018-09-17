package com.ivianuu.compass.playground

import android.os.Bundle
import com.ivianuu.compass.Destination

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DummyFragment::class)
data class BundleDestination(val bundle: Bundle)