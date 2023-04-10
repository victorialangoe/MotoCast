package com.example.motocast.ui.view.map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.motocast.MainActivity
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel

@Composable
fun MapView(
    mapLocationViewModel: MapLocationViewModel,
    activity: MainActivity
) {
    val mapUiState by mapLocationViewModel.uiState.collectAsState()

    mapLocationViewModel.run {
        loadMapView(activity.applicationContext)
        cameraToUserLocation()
    }

    if (mapUiState.mapView != null) {
        MapViewContent(mapView = mapUiState.mapView!!) { mapLocationViewModel.cameraToUserLocation() }
    } else {
        Text(text = "Loading map...")
    }
}


