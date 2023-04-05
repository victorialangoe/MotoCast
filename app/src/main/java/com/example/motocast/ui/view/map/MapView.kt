package com.example.motocast.ui.view.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.motocast.MainActivity
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.map.MapUiState
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.location.LocationViewModel

@Composable
fun MapView(viewModel: MapViewModel, locationViewModel: LocationViewModel, activity: MainActivity) {
    val mapUiState: MapUiState by viewModel.uiState.collectAsState()
    viewModel.addMapView(createMap(viewModel = viewModel, activity = activity))
    viewModel.cameraToUserLocation(locationViewModel)

    if (mapUiState.mapView != null) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val mapboxMap = createRef()
            val fab = createRef()


            AndroidView(
                factory = { mapUiState.mapView!! },
                modifier = Modifier.constrainAs(mapboxMap) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            FloatingActionButton(
                onClick = {
                    viewModel.cameraToUserLocation(locationViewModel)
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
    } else {
        Text(text = "Loading map...")
    }
}
