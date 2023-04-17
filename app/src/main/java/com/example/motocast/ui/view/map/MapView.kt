package com.example.motocast.ui.view.map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mapbox.maps.MapView

@Composable
fun MapView(
    geoJsonData: String? = null,
    mapView: MapView? = null,
    drawGeoJson: (String) -> Unit,
    onInit: () -> Unit,
    ) {

    onInit()

    if (geoJsonData != null) {
        drawGeoJson(geoJsonData)
    }

    if (mapView != null) {
        MapViewContent(mapView)
    } else {
        Text(text = "Loading map...")
    }
}


