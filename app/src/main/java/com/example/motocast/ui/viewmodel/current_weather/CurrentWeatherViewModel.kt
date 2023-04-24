package com.example.motocast.ui.viewmodel.current_weather

import androidx.lifecycle.ViewModel
import com.example.motocast.domain.use_cases.FetchNowCastDataUseCase
import com.example.motocast.domain.use_cases.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
    private val getLocationUseCase: GetLocationUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val location = getLocationUseCase()
            if (location != null) {
                fetchNowCastDataEvery5min(
                    location.latitude,
                    location.longitude
                )
            }
        }
    }

    private fun fetchNowCastDataEvery5min(
        latitude: Double,
        longitude: Double
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val weatherData = fetchNowCastDataUseCase(
                    latitude,
                    longitude
                )
                if (weatherData != null) {
                    _uiState.value = WeatherUiState(
                        symbolCode = weatherData.symbolCode,
                        temperature = weatherData.temperature,
                        windSpeed = weatherData.windSpeed,
                        windDirection = weatherData.windDirection,
                        updatedAt = weatherData.updatedAt
                    )
                }
                delay(60 * 1000 * 5) // 5 minutes
            }
        }
    }
}

