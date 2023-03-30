package com.example.motocast.ui.map

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.motocast.R
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.Plugin


@Composable
fun MapWrapper() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val mapboxMap = createRef()
        val fab = createRef()
        val mapView = rememberMapViewWithLifecycle()


        AndroidView(
            factory = {mapView},
            modifier = Modifier.constrainAs(mapboxMap) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        FloatingActionButton(
            onClick = {

            },
            modifier = Modifier
                .padding(25.dp)
                .width(50.dp)
                .height(50.dp)
                .constrainAs(fab) {
                    end.linkTo(mapboxMap.end)
                    bottom.linkTo(mapboxMap.bottom)
                }
        ) {

        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        Mapbox.getInstance(
            context,
            context.getString(R.string.mapbox_access_token)
        )
        MapView(context).apply {
            val mapView = this
            getMapAsync { mapboxMap ->
                mapboxMap.setStyle(Style.MAPBOX_STREETS)

                val position = CameraPosition.Builder()
                    .target(LatLng(70.04004014308637, -20.744085852141072))
                    .zoom(15.0)
                    .build()

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1)

                mapboxMap.getStyle {
                }
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        // Make MapView follow the current lifecycle
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

/**
 * Handles lifecycle of provided mapView
 */
private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }