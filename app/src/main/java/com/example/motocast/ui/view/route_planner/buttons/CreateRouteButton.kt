package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun CreateRouteButton(
    navController: NavController,
    routePlannerViewModel: RoutePlannerViewModel
) {
    Button(
        onClick = {
            if (routePlannerViewModel.checkIfAllDestinationsHaveNames()) {
                routePlannerViewModel.start()
                navController.navigate("home")
            }
        },
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 55.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ferdig",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }
    }
}