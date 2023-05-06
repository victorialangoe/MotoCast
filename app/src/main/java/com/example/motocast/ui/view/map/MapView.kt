package com.example.motocast.ui.view.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.motocast.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.example.motocast.theme.Blue700
import com.example.motocast.theme.Red700

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
        }

        for (waypoint in waypoints) {
            val point = Point.fromLngLat(
                waypoint.longitude ?: 0.0,
                waypoint.latitude ?: 0.0
            )
            addViewAnnotation(context = context, point = point, viewAnnotationManager = viewAnnotationManager, waypoint = waypoint)
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
        location = waypoint.name ?: "Ukjent",
        time = waypoint.timestamp,
        iconSymbol = waypoint.weather?.symbolCode ?: ""
    )

    // Measure the view to get the correct width and height

    viewAnnotationManager.addViewAnnotation(
        view,
        viewAnnotationOptions {
            geometry(point)
            allowOverlap(true) // Allow annotation to overlap with other annotations
            //offsetY(100) WE MAY USE THIS ON ANTHER VIEW
        }
    )
}















