package com.example.motocast.ui.view.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.mapbox.maps.MapView

@Composable
fun MapViewContent(
    mapView: MapView,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val mapboxMap = createRef()

        AndroidView(
            factory = { mapView },
            modifier = Modifier.constrainAs(mapboxMap) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                }
                .padding(bottom = 180.dp)
        )


    }
}
