package com.example.motocast.ui.viewmodel.route_planner

import java.util.*

data class RoutePlannerUiState (
    val isLoading: Boolean = false,
    // Every route must have at least two destinations
    val destinations: List<Destination> = List(2) {
        Destination(
            name = null,
            latitude = 0.0,
            longitude = 0.0,
            timestamp = 0
        )
    },
    // TODO: USE api level 33 ?
    val startTime: Calendar? = null,
    val activeDestinationIndex : Int = 0,
    val error: String? = null
)
data class Destination (
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long,
)

