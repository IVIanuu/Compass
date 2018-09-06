package com.ivianuu.compass.sample

import android.os.Bundle
import com.ivianuu.compass.Destination

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DummyFragment::class)
data class BundleDestination(val bundle: Bundle)