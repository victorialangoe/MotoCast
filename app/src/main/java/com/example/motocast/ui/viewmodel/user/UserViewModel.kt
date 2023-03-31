package com.example.motocast.ui.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.MainActivity
import com.example.motocast.util.getCurrentLocation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

/**
 * For the user's location, fetch the data, using the [getCurrentLocation] function.
 */
class UserViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())

    val uiState: StateFlow<UserUiState> = _uiState

    private var job: Job? = null // Declare a variable to hold the job

    /**
     * Start fetching the data every 15 seconds, and update the UI.
     */
    fun startFetchingUserData(activity: MainActivity) {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) { // Use a loop to keep fetching the data every 15 seconds
                fetchUserData(activity)
                delay(15 * 1000) // Wait for 15 seconds before fetching again
            }
        }
    }

    /**
     * Stop fetching the data.
     * This method should be called when the user leaves the app or the screen.
     */
    fun stopFetchingUserData() {
        job?.cancel() // Cancel the job to stop fetching the data
        job = null
    }

    /**
     * Callback function to be called when the user data is fetched.
     * It is not private, because we need to call it from the MainActivity once.
     */
    private fun fetchUserData(activity: MainActivity) {
        val currentUiState = _uiState.value
        if (currentUiState.isLoading) return // If the data is already being fetched, return

        _uiState.value = currentUiState.copy(isLoading = true) // Set isLoading to true

        getCurrentLocation(
            activity = activity,
            context = activity.applicationContext,
            onSuccess = {
                _uiState.value = currentUiState.copy(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    isLoading = false,
                    updatedAt = Date().toString(),
                )
                Log.d("UserViewModel", "Location: ${it.latitude}, ${it.longitude}")
            }, onError = {
                _uiState.value = currentUiState.copy(error = it.toString(), isLoading = false)
                Log.d("UserViewModel", "Error: $it")
            }
        )
    }
}
