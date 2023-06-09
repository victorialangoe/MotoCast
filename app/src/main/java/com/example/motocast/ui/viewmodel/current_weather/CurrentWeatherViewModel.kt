package com.example.motocast.ui.viewmodel.current_weather

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

    private var fetchNowCastDataJob: Job? = null
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        startFetchingNowCastData()
    }

    fun startFetchingNowCastData() {
        if (fetchNowCastDataJob == null || fetchNowCastDataJob?.isCancelled == true) {
            fetchNowCastDataJob = fetchNowCastDataEvery5min()
        }
    }

    fun stopFetchingNowCastData() {
        fetchNowCastDataJob?.cancel()
    }

    private fun fetchNowCastDataEvery5min(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                var location = locationUseCase.getCurrentLocation()
                var retries = 0

                while (location == null && retries < 10) {
                    /** This is a hack to get the location if it is null. Sometimes it is null
                     * when the app first starts. Technically debt, for sure.
                     *
                     * A potential fix would be to add this in the LocationUseCase, because then
                     * we could use the same logic in other places where we need to get the location.
                     * */
                    delay(1000)
                    location = locationUseCase.getCurrentLocation()
                    retries++
                    Log.d("CurrentWeatherViewModel", "Retry $retries: Location: $location")
                }

                var weatherData: WeatherUiState? = null
                if (location != null) {
                    weatherData = fetchNowCastDataUseCase(location.latitude, location.longitude)
                }

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