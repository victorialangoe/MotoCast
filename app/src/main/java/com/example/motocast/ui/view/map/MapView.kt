package com.example.motocast.ui.view.map

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.motocast.R
import com.mapbox.maps.MapView

@Composable
fun MapView(
    mapView: MapView? = null,
    onInit: () -> Unit,
) {

    onInit()

    if (mapView != null) {
        MapViewContent(mapView)
    } else {
        Text(
            text = stringResource(R.string.loading_map),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}
