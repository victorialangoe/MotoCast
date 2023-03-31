package com.example.motocast.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.motocast.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.LocationProvider
import com.mapbox.maps.plugin.locationcomponent.location

import com.mapbox.maps.plugin.locationcomponent.location2


@Composable
fun MapWrapper() {
    val context = LocalContext.current
    // Configure the default ResourceOptionsManager with your custom access token
    ResourceOptionsManager.getDefault(context, context.getString(R.string.mapbox_access_token))

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
        MapView(context).apply {
            val mapView = this
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
                val mapboxMap = mapView.getMapboxMap()
                mapView.location.apply {
                    enabled = true
                    pulsingEnabled = true
                }
                val cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(10.717173333333333, 59.942881666666665))
                    .zoom(15.0)
                    .build()
                mapboxMap.setCamera(cameraOptions)
            }
        }
    }
    return mapView
}



