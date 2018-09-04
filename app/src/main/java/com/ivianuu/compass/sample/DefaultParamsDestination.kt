package com.ivianuu.compass.sample

import androidx.fragment.app.Fragment
import com.ivianuu.compass.Destination

/**
 * @author Manuel Wrage (IVIanuu)
 */
@Destination(DefaultParamsFragment::class)
data class DefaultParamsDestination(
    val name: String = "",
    val age: Int = 0,
    val score: Float = 0f
)

class DefaultParamsFragment : Fragment() {
    init {
    }
}