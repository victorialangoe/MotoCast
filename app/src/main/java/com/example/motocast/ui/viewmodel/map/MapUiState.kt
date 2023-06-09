package com.example.motocast.ui.viewmodel.map

import android.location.Location
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.maps.MapView

data class MapUiState(
    val isLoading : Boolean = false,
    val isInitialised: Boolean = false,
    val mapView: MapView? = null,
    val lastLocation: Location? = null,
    val trackUserOnMap: Boolean = false,
    val mapBottomOffset: Int = 50,
    var previousWaypoints: List<RouteWithWaypoint> = listOf(),
)