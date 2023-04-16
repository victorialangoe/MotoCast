package com.example.motocast.ui.viewmodel.route_planner

import com.example.motocast.ui.viewmodel.weather.WeatherUiState
import com.mapbox.geojson.FeatureCollection
import java.time.Duration
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
    val startTime: TimeAndDateUiState = TimeAndDateUiState(),
    val activeDestinationIndex : Int = 0,
    val error: String? = null,
    val geoJsonData: String? = null,
    val duration: Double = 0.0, // in seconds
    val distance: Double = 0.0, // in meters
    val waypoints: List<RouteWithWaypoint> = listOf(),
)

data class RouteWithWaypoint (
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timeFromStart: Double?,
    val timestamp: Calendar?,
    var weatherUiState: WeatherUiState = WeatherUiState(),
)

data class TimeAndDateUiState (
    val timePickerUiState: TimePickerUiState = TimePickerUiState(),
    val datePickerUiState: DatePickerUiState = DatePickerUiState()
)
data class TimePickerUiState (
    val hour: Int = 0,
    val minute: Int = 0,
)

data class DatePickerUiState (
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0
)

data class Destination (
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long,
)

