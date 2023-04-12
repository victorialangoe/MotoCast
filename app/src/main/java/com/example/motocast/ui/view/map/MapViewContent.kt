package com.example.motocast.ui.view.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.motocast.R
import com.mapbox.maps.MapView

@Composable
fun MapViewContent(
    mapView: MapView,
    cameraToUserLocation: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val mapboxMap = createRef()
        val fab = createRef()

        AndroidView(
            factory = { mapView },
            modifier = Modifier.constrainAs(mapboxMap) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })


    }
}
