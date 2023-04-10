package com.example.motocast.ui.viewmodel.route_planner

data class RoutePlannerUiState (
    val isLoading: Boolean = false,
    // Every route must have at least two destinations
    val destinations: List<Destination> = List(2) { index ->
        Destination("", 0.0, 0.0, 0)
    },
    val startTimestamp: Long? = null,
)

data class Destination (
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
)

