package com.example.motocast.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())

    val uiState = _uiState

    fun setScreenMode(screenMode: ScreenMode) {
        _uiState.value = _uiState.value.copy(screenMode = screenMode)
    }
}