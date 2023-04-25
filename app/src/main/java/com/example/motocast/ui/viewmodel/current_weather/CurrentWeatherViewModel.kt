package com.example.motocast.ui.viewmodel.current_weather

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.domain.use_cases.FetchNowCastDataUseCase
import com.example.motocast.domain.use_cases.LocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
    private val locationUseCase: LocationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    private var job: Job? = null


    fun startFetchingNowCastData() {

    }

    fun stopFetchingNowCastData() {
    }

    private fun fetchNowCastDataEvery5min() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val location = locationUseCase.getCurrentLocation()
                Log.d("CurrentWeatherViewModel", "Location: $location")
                var weatherData: WeatherUiState? = null
                if (location != null) {
                    weatherData = fetchNowCastDataUseCase(location.latitude, location.longitude)
                }

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

