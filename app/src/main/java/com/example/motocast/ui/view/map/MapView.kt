package com.example.motocast.ui.view.map

import android.content.Context
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
    drawGeoJson: (String) -> Unit,
    onInit: () -> Unit,
    waypoints: List<RouteWithWaypoint>,
    context: Context
) {

    onInit()


    if (geoJsonData != null) {
        drawGeoJson(geoJsonData)
    }

    if (mapView != null) {
        MapViewContent(mapView)
        val viewAnnotationManager = mapView.viewAnnotationManager
        val previousWaypoints = remember { mutableStateOf(emptyList<RouteWithWaypoint>()) }

        if (previousWaypoints.value != waypoints) {
            viewAnnotationManager.removeAllViewAnnotations()
            previousWaypoints.value = waypoints
            for (waypoint in waypoints) {
                val point = Point.fromLngLat(
                    waypoint.longitude ?: 0.0,
                    waypoint.latitude ?: 0.0
                )
                addViewAnnotation(context = context, point = point, viewAnnotationManager = viewAnnotationManager, waypoint = waypoint)
            }
        }
    } else {
        Text(
            text = stringResource(R.string.loading_map),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}
private fun addViewAnnotation(
    point: Point,
    viewAnnotationManager: ViewAnnotationManager,
    waypoint: RouteWithWaypoint,
    context: Context
) {
    val view = ComposableWrapperView(
        context = context,
        temperature = (waypoint.weather?.temperature?.toInt() ?: 0),
        time = waypoint.timestamp,
        iconSymbol = waypoint.weather?.symbolCode ?: ""
    )

    // Measure the view to get the correct width and height

    if (viewAnnotationManager != null) {

        viewAnnotationManager.addViewAnnotation(
            view,
            viewAnnotationOptions {
                geometry(point)
                allowOverlap(true)
                offsetY(100) // WE MAY USE THIS ON ANTHER VIEW
            }
        )
    }


}















