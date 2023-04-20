package com.example.motocast.ui.viewmodel.weather

import com.example.motocast.data.model.MetAlertsDataModel

data class WeatherUiState (
    val isLoading: Boolean = false,
    val symbolCode: String? = null,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val error: String? = null,
    val updatedAt: String? = null,
    val alerts: MetAlertsDataModel? = null
)
