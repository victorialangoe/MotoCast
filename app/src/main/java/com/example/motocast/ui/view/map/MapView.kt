package com.example.motocast.ui.view.map

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mapbox.maps.MapView

@Composable
fun MapView(
    geoJsonData: String? = null,
    mapView: MapView? = null,
    drawGeoJson: (String) -> Unit,
    onInit: () -> Unit,
    bottomOffset: Int = 0,
) {

    onInit()

    if (geoJsonData != null) {
        drawGeoJson(geoJsonData)
    }

    if (mapView != null) {
        MapViewContent(mapView, bottomOffset)
    } else {
        Text(
            text = "Loading map...",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


