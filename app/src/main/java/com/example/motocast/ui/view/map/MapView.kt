package com.example.motocast.ui.view.map

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun MapView(
    geoJsonData: String? = null,
    mapView: MapView? = null,
    drawGeoJson: (String, List<RouteWithWaypoint>) -> Unit,
    onInit: () -> Unit,
    waypoints: List<RouteWithWaypoint>,
) {

    onInit()


    if (geoJsonData != null) {
        drawGeoJson(geoJsonData, waypoints)
    }

    if (mapView != null) {
        MapViewContent(mapView)
    } else {
        Text(
            text = stringResource(R.string.loading_map),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}
