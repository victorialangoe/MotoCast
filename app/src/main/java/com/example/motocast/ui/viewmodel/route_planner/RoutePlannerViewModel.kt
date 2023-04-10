package com.example.motocast.ui.viewmodel.route_planner

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.ui.viewmodel.address.Address
import kotlinx.coroutines.flow.MutableStateFlow

class RoutePlannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoutePlannerUiState())

    val uiState = _uiState

    /* This method is only used for debugging purposes */
    private fun printDestinations() {
        // create a string with - between each destination
        _uiState.value.destinations.forEachIndexed { index, destination ->
            Log.d(
                "RoutePlannerViewModel", "Destination $index: ${
                    "name: ${destination.name}, " +
                            "latitude: ${destination.latitude}, " +
                            "longitude: ${destination.longitude}, " +
                            "timestamp: ${destination.timestamp}"
                }"
            )
        }
    }

    fun getTotalDestinations(): Int {
        return _uiState.value.destinations.size
    }

    fun addDestination() {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        // max 5 destinations
        if (newDestinations.size < 5) {
            newDestinations.add(Destination(null, 0.0, 0.0, 0))
            _uiState.value = currentUiState.copy(destinations = newDestinations)
            printDestinations()
        }
    }

    fun setActiveDestinationIndex(index: Int) {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(activeDestinationIndex = index)
        printDestinations()
    }

    fun updateDestination(index: Int, address: Address) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        val destination = newDestinations[index]
        newDestinations[index] = destination.copy(
            name = address.addressText,
            latitude = address.latitude,
            longitude = address.longitude,
        )
        _uiState.value = currentUiState.copy(destinations = newDestinations)

        printDestinations()
    }

    fun removeDestination(index: Int) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        Log.d("RoutePlannerViewModel", "Removing destination at index $index")
        Log.d("RoutePlannerViewModel", "Current destinations: ${newDestinations.size}")
        // min 2 destinations
        if (newDestinations.size > 2) {
            newDestinations.removeAt(index)
            _uiState.value = currentUiState.copy(destinations = newDestinations)
            printDestinations()
        }
    }

    /**
     * Clears all destinations and resets the start timestamp
     */
    fun clear() {
        _uiState.value = RoutePlannerUiState()
        printDestinations()

    }

    fun checkIfAllDestinationsHaveNames(): Boolean {
        val currentUiState = _uiState.value
        currentUiState.destinations.forEach {
            if (it.name == null || it.name == "") {
                return false
            }
        }
        return true
    }

    /**
     * Starts the route planning
     */
    fun start() {
        val currentUiState = _uiState.value
        // TODO: A viewmodel for errors?
        if (!checkIfAllDestinationsHaveNames()) {
            _uiState.value = currentUiState.copy(error = "All destinations must have a name")
            return
        }

        printDestinations()
    }
}

