package com.example.motocast.ui.viewmodel.nowcast

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.internal.wait

/**
 * Provides the data to the NowCastScreen via the uiState variable.
 * The data is fetched every 5 minutes.
 */
class NowCastViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NowCastUiState())
    private var job: Job? = null
    private val nowCastDataSource = NowCastDataSource()

    val uiState: StateFlow<NowCastUiState> = _uiState

    /**
     * Start fetching the data every 5 minutes, and update the UI.
     * The data is fetched every 5 minutes because the API is updated every 5 minutes.
     *
     * We only want to fetch the data if the user is in the area where the API (Scandinavia) is valid.
     *  Maximum Latitude: 71.18°N (Nordkinn Peninsula, Norway)
     *  Minimum Latitude: 54.50°N (Kattegat, Denmark/Sweden)
     *  Maximum Longitude: 31.10°E (Varangerfjord, Norway)
     *  Minimum Longitude: 0.10°E (Skagen, Denmark)
     */
    fun startFetchingNowCastData(
        mapLocationViewModel: MapLocationViewModel
    ) {
        Log.d("NowCastViewModel", "Start fetching the data")

        job = CoroutineScope(Dispatchers.IO).launch {
            Log.d("NowCastViewModel", "Inside the coroutine")
            while (isActive) { // Use a loop to keep fetching the data every 5 minutes
                // Wait for the user location to be fetched
                Log.d("NowCastViewModel", "Waiting for the user location")
                mapLocationViewModel.getCurrentLocation(
                    onSuccess = { location ->
                        // Check if the user is in Scandinavia
                        if (location.latitude in 54.50..71.18 && location.longitude in 0.10..31.10) {
                            Log.d("NowCastViewModel", "User is in Scandinavia")
                            fetchNowCastData(location.latitude, location.longitude)
                        } else {
                            Log.e("NowCastViewModel", "User is not in Scandinavia")
                        }

                    },
                    onError = { error ->
                        Log.e("NowCastViewModel", "Error fetching the user location: $error")
                    }
                )
                delay(5 * 60 * 1000) // Wait for 5 minutes before fetching again
            }
        }
    }

    /**
     * Stop fetching the data.
     * This method should be called when the user leaves the app or the screen.
     */
    fun stopFetchingNowCastData() {
        job?.cancel() // Cancel the job to stop fetching the data
        job = null
    }

    /**
     * Fetch the data from the API and update the UI.
     */
    fun fetchNowCastData(latitude: Double, longitude: Double) {
        val currentUiState = _uiState.value
        if (currentUiState.isLoading) {
            Log.d("NowCastViewModel", "Already fetching data")
            return
        }

        _uiState.value = currentUiState.copy(isLoading = true)

        nowCastDataSource.getNowCastData(
            latitude,
            longitude,
            onSuccess = {
                Log.d("NowCastViewModel", "Fetched NowCast data ${it.properties}")
                _uiState.value = currentUiState.copy(
                isLoading = false,
                symbolCode = it.properties.timeseries.first().data.next_1_hours.summary.symbol_code,
                temperature = it.properties.timeseries.first().data.instant.details.air_temperature,
                windSpeed = it.properties.timeseries.first().data.instant.details.wind_speed,
                windDirection = it.properties.timeseries.first().data.instant.details.wind_from_direction,
                updatedAt = it.properties.meta.updated_at
            )
        }, onError = {
            _uiState.value = currentUiState.copy(error = it, isLoading = false)
            Log.d("NowCastViewModel", "Error fetching NowCast data: $it")
        })
    }

    fun getTemperature(
        latitude: Double?,
        longitude: Double?,
        onSuccess: (Double?) -> Unit)
    {
        var temperature: Double?

        nowCastDataSource.getNowCastData (
            latitude?: 0.0,
            longitude?: 0.0,
            onSuccess = {
                temperature = it.properties.timeseries.first().data.instant.details.air_temperature

                Log.d("NowCastViewModel", "Got NowCast temperature: $temperature")
                onSuccess(temperature)
            },
            onError = {
                Log.e("NowCastViewModel", "Error getting NowCast temperature: $it")
            }
        )
    }
}

