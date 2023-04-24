package com.example.motocast.ui.viewmodel.settings

data class SettingsUiState(
    val darkMode: Boolean = false,
    val mapStyle: String = "mapbox://styles/mapbox/streets-v11",
    val userName: String = "",
)
