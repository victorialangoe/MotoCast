package com.example.motocast.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.motocast.MainActivity
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun createMap(viewModel: MapViewModel, activity: MainActivity): MapView {

    val mapView = remember {
        MapView(activity.applicationContext).apply {
            val mapView = this
            mapView.getMapboxMap().loadStyleUri("mapbox://styles/mapbox/navigation-day-v1") {
                mapView.location.apply {
                    enabled = true
                    pulsingEnabled = true
                }

            }
        }
    }

    viewModel.updateIsLoading(false)
    return mapView
}