package com.example.motocast.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import com.example.motocast.domain.use_cases.GetSystemDarkModeEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSystemDarkModeEnabledUseCase: GetSystemDarkModeEnabledUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val darkMode = isSystemDarkModeEnabled()
            setDarkMode(darkMode)
        }
    }

    fun setUserName(userName: String) {
        _uiState.value = _uiState.value.copy(userName = userName)
    }

    fun setDarkMode(darkMode: Boolean) {
        _uiState.value = _uiState.value.copy(darkMode = darkMode)
    }

    private suspend fun isSystemDarkModeEnabled(): Boolean {
        return getSystemDarkModeEnabledUseCase()
    }
}
