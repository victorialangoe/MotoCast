package com.example.motocast.ui.viewmodel.current_weather

import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.Properties

/**
 * [WeatherUiState] represents the state of the weather UI for a single location. It includes
 * various properties for displaying weather conditions at a given location.
 *
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val symbolCode: String? = null,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val error: String? = null,
    val updatedAt: String? = null,
    val alerts: MetAlertsDataModel? = null
)

/**
 * [RouteWeatherUiState] represents the state of the weather UI along a certain
 * route. Even though it shares most of the properties with [WeatherUiState], the difference lies
 * in the 'alerts' property. In [RouteWeatherUiState], 'alerts' is a list because there might be
 * multiple weather alerts along a route, while in [WeatherUiState] it's a single instance because
 * there's only one location involved.
 */
data class RouteWeatherUiState(
    val isLoading: Boolean = false,
    val symbolCode: String? = null,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val error: String? = null,
    val updatedAt: String? = null,
    val alerts: List<Properties>? = null
)

