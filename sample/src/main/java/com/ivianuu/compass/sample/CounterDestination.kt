package com.ivianuu.compass.sample

import com.ivianuu.compass.Destination
import com.ivianuu.compass.Detour

@Detour(CounterFragmentDetour::class)
@Destination(CounterFragment::class)
data class CounterDestination(val count: Int, val color: Int)

