package com.example.motocast.ui.viewmodel.route_planner

data class RoutePlannerUiState (
    val isLoading: Boolean = false,
    val destinations: List<Destination> = emptyList(),
    val startTimestamp: Long? = null,
)

data class Destination (
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
)

