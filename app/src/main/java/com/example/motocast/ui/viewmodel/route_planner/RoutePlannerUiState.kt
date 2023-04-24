package com.example.motocast.ui.viewmodel.route_planner

import com.example.motocast.ui.viewmodel.current_weather.RouteWeatherUiState
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
    val startTime: Calendar = Calendar.getInstance(),
    val activeDestinationIndex : Int = 0,
    val error: String? = null,
    val geoJsonData: String? = null,
    val duration: Double = 0.0, // in seconds
    val durationAsString: String = "",
    val distance: Double = 0.0, // in meters
    val waypoints: List<RouteWithWaypoint> = listOf(),
)

data class RouteWithWaypoint (
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timeFromStart: Double?,
    val timestamp: Calendar?,
    val isInDestination: Boolean = false,
    val weather: RouteWeatherUiState? = null,
)

data class Destination (
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long,
)

