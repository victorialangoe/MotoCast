package com.example.motocast.ui.map

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.motocast.R
import com.example.motocast.util.getCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun MapWrapper(activity: Activity) {
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
                cameraToUserLocation(mapView, activity)
            },
            modifier = Modifier
                .padding(25.dp)
                .width(50.dp)
                .height(50.dp)
                .zIndex(1f)
                .constrainAs(fab) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            containerColor = Color.White,
            shape = CircleShape,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_location_searching_24),
                contentDescription = "Location icon",
                tint = Color.Black
            )
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


fun cameraToUserLocation(mapView: MapView, activity: Activity) {

    val mapboxMap = mapView.getMapboxMap()
    Log.d("MapActivity", "Camera to user location")

    CoroutineScope(Dispatchers.IO).launch {
        Log.d("MapActivity", "CorutineScope")
        // Get the current location using the getCurrentLocation function
        getCurrentLocation(
            activity = activity,
            context = activity.applicationContext,
            onSuccess = { location ->
                Log.d("MapActivity", "Location: ehiehieheih")
                // Create a CameraOptions object with the user's location as the center
                val cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(location.longitude, location.latitude))
                    .zoom(15.0)
                    .build()

                // Set the camera to the new CameraOptions
                mapboxMap.setCamera(cameraOptions)
            },
            onError = { exception ->
                // Handle the error, e.g., show a message to the user or log the error
                Log.d("MapActivity", "Error: ${exception.localizedMessage}")
            }
        )
        Log.d("MapActivity", "ferdigs")
    }

}










