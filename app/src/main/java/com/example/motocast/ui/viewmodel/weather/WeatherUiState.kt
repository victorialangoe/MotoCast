package com.example.motocast.ui.viewmodel.weather
data class WeatherUiState (
    val isLoading: Boolean = false,
    val symbolCode: String? = null,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val error: String? = null,
    val updatedAt: String? = null,
)
