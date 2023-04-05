package com.example.motocast.ui.view.home_bottom_scaffold
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.location.LocationViewModel


@Composable
fun LocateUserBadge(
    mapViewModel: MapViewModel,
    locationViewModel: LocationViewModel
){
    Button(
    onClick = {
        mapViewModel.cameraToUserLocation(locationViewModel)
    },
    shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .size(32.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_location_searching_24),
            contentDescription = "Location icon",
            tint = Color.Black
        )
    }
}
