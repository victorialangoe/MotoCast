package com.example.motocast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView
import com.example.motocast.ui.MapViewFunctions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = rememberMapView()

    AndroidView(
        factory = { context ->
            // Handle permissions first, before map is created. not depicted here

            // Initialize the MapView using the MapViewFunctions object
            MapViewFunctions.initMapView(context, mapView)

            mapView
        }
    )

    DisposableEffect(lifecycleOwner) {
        MapViewFunctions.bindMapView(mapView, lifecycleOwner)

        onDispose {
            MapViewFunctions.unbindMapView(lifecycleOwner)
        }
    }
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    return remember { MapView(context) }
}
