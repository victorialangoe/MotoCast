package com.example.motocast.ui.viewmodel.settings

data class SettingsUiState(
    val screenMode: ScreenMode = ScreenMode.PREFER_SYSTEM,
    val mapStyle: String = "mapbox://styles/mapbox/streets-v11",
)

enum class ScreenMode {
    DARK,
    LIGHT,
    PREFER_SYSTEM
}