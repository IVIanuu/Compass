package com.ivianuu.compass.sample

import android.support.v4.app.Fragment
import com.ivianuu.compass.Destination

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(OuterFragment::class)
data class OuterDestination(val value: String) {

    @Destination(InnerFragment::class)
    data class InnerDestination(val value: String)

}

class OuterFragment : Fragment()

class InnerFragment : Fragment()