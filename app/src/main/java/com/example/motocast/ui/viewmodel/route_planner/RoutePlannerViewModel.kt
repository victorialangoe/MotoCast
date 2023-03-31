package com.example.motocast.ui.viewmodel.route_planner

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RoutePlannerViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RoutePlannerUiState())

    val uiState = _uiState

    // This method is only used for debugging purposes
    private fun printDestinations() {
        val currentUiState = _uiState.value
        // create a string with - between each destination
        val destinationsString = currentUiState.destinations.joinToString("-") { it.name }
        Log.d("RoutePlannerViewModel", "Destinations: $destinationsString")
    }

    fun addDestination() {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        newDestinations.add(Destination("Destination ${newDestinations.size + 1}", 0.0, 0.0, 0))
        _uiState.value = currentUiState.copy(destinations = newDestinations)
        printDestinations()
    }

    fun removeDestination(index: Int) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        newDestinations.removeAt(index)
        _uiState.value = currentUiState.copy(destinations = newDestinations)
        printDestinations()
    }

    fun updateDestination(oldIndex: Int, newIndex: Int) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        val destination = newDestinations.removeAt(oldIndex)
        newDestinations.add(newIndex, destination)
        _uiState.value = currentUiState.copy(destinations = newDestinations)
        printDestinations()

    }

    fun updateStartTimestamp(timestamp: Long) {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(startTimestamp = timestamp)
        printDestinations()

    }

    fun clear() {
        _uiState.value = RoutePlannerUiState()
        printDestinations()

    }

    fun start() {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(isLoading = true)
        printDestinations()

    }
}
