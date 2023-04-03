package com.example.motocast.ui.viewmodel.nowcast

data class NowCastUiState (
    val isLoading: Boolean = true,
    val symbolCode: String? = null,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val error: String? = null,
    val updatedAt: String? = null,
)
