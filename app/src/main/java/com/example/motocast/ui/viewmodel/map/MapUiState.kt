package com.example.motocast.ui.viewmodel.map

import com.mapbox.maps.MapView

data class MapUiState(
    val isLoading : Boolean = false,
    val mapView: MapView? = null,
)