package com.example.motocast.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(context: Context) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        // Get the system dark mode setting
        val darkMode = isSystemDarkModeEnabled(context)
        setDarkMode(darkMode)
    }

    fun setDarkMode(darkMode: Boolean) {
        _uiState.value = _uiState.value.copy(darkMode = darkMode)
    }

    private fun isSystemDarkModeEnabled(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}
