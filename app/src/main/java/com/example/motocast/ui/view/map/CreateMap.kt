package com.example.motocast.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.motocast.MainActivity
import com.example.motocast.ui.viewmodel.map.MapUiState
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.util.getCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun createMap(viewModel: MapViewModel, activity: MainActivity): MapView {

    val mapView = remember {
        MapView(activity.applicationContext).apply {
            val mapView = this
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
                val mapboxMap = mapView.getMapboxMap()
                mapView.location.apply {
                    enabled = true
                    pulsingEnabled = true
                }
                val cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(10.0, 10.0))
                    .zoom(15.0)
                    .build()
                mapboxMap.setCamera(cameraOptions)
            }
        }
    }

    viewModel.updateIsLoading(false)
    return mapView
}