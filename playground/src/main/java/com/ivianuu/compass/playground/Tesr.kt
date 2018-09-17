package com.ivianuu.compass.playground

import com.ivianuu.compass.Destination

@Destination(DummyFragment::class)
data class HistoryDestination(
    val isHome: Boolean = true,
    val timestamps: LongArray = longArrayOf(),
    val favorites: Boolean = false,
    val newTracks: Boolean = false,
    val newTracksSince: Long = -1L,
    val enterAnimation: Boolean = true,
    val searchable: Boolean = false
)