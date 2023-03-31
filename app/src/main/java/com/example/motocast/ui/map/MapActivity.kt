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
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style


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
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS){
                val mapboxMap = this

            }
        }
    }

    return mapView
}


