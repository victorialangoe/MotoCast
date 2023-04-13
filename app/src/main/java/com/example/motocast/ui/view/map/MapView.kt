package com.example.motocast.ui.view.map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.motocast.MainActivity
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun MapView(
    mapLocationViewModel: MapLocationViewModel,
    activity: MainActivity,
    routePlannerViewModel: RoutePlannerViewModel
) {
    val mapUiState by mapLocationViewModel.uiState.collectAsState()
    val routeUiState by routePlannerViewModel.uiState.collectAsState()

    mapLocationViewModel.run {
        loadMapView(activity.applicationContext)
        cameraToUserLocation()
    }
    if (routeUiState.geoJsonData != null) {
        mapLocationViewModel.drawGeoJson(routeUiState.geoJsonData!!)
    }



    if (mapUiState.mapView != null) {
        MapViewContent(mapView = mapUiState.mapView!!) { mapLocationViewModel.cameraToUserLocation() }
    } else {
        Text(text = "Loading map...")
    }
}


