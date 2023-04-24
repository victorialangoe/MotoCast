package com.example.motocast.ui.viewmodel.current_weather

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.domain.use_cases.FetchNowCastDataUseCase
import com.example.motocast.domain.use_cases.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
    private val getLocationUseCase: GetLocationUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    private var job: Job? = null

    fun startFetchingNowCastData() {
        job = CoroutineScope(Dispatchers.IO).launch(){
            var location = getLocationUseCase()
            // Location might be null if the user has not granted location permission
            while (location == null) {
                Log.d("CurrentWeatherViewModel", "Location is null")
                delay(2000)
                location = getLocationUseCase()
            }
            fetchNowCastDataEvery5min(
                location.latitude,
                location.longitude
            )
        }
    }

    fun stopFetchingNowCastData() {
        job?.cancel()
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
                Log.d("CurrentWeatherViewModel", "Weather data: $weatherData")
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

