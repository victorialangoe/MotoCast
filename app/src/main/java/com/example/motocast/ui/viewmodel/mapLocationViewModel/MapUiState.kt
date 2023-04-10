package com.example.motocast.ui.viewmodel.mapLocationViewModel

import android.location.Location
import com.mapbox.maps.MapView

data class MapUiState(
    val isLoading : Boolean = false,
    val mapView: MapView? = null,
    val lastLocation: Location? = null,
)