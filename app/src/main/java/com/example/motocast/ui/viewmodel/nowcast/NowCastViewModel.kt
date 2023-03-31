package com.example.motocast.ui.viewmodel.nowcast

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.ui.viewmodel.user.UserViewModel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Provides the data to the NowCastScreen via the uiState variable.
 * The data is fetched every 5 minutes.
 */
class NowCastViewModel(private val userViewModel: UserViewModel) : ViewModel() {

    private val _uiState = MutableStateFlow(NowCastUiState())
    val uiState: StateFlow<NowCastUiState> = _uiState

    private var job: Job? = null

    init {
        // Observe the state flow in userViewModel
        userViewModel.uiState
            .distinctUntilChangedBy { it.latitude to it.longitude }
            .onEach { fetchNowCastData() }
            .launchIn(viewModelScope)
    }


    /**
     * Start fetching the data every 5 minutes, and update the UI.
     * The data is fetched every 5 minutes because the API is updated every 5 minutes.
     */
    fun startFetchingNowCastData() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) { // Use a loop to keep fetching the data every 5 minutes
                fetchNowCastData()
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
    private fun fetchNowCastData() {
        val currentUiState = _uiState.value
        if (currentUiState.isLoading) return // Do not fetch data while loading

        _uiState.value = currentUiState.copy(isLoading = true)
        NowCastDataSource().getNowCastData(userViewModel.uiState.value.latitude, userViewModel.uiState.value.longitude, onSuccess = {
            _uiState.value = currentUiState.copy(
                weatherData = it,
                updatedAt = it.properties.meta.updated_at,
                isLoading = false
            )
            Log.d("NowCastViewModel", "Fetched NowCast data ${it.properties}")
        }, onError = {
            _uiState.value = currentUiState.copy(error = it, isLoading = false)
            Log.d("NowCastViewModel", "Error fetching NowCast data: $it")
        })
    }
}

