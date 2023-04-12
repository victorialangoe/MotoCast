package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun AddFieldButton(
    routePlannerViewModel: RoutePlannerViewModel,
    navController: NavController,

) {
    OutlinedButton(
        onClick = {
            routePlannerViewModel.addDestination()
            routePlannerViewModel.setActiveDestinationIndex(routePlannerViewModel.getTotalDestinations() - 1)
            navController.navigate("add_destination_screen")
        },
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 55.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Legg til stopp",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                contentDescription = "Add a stop button"
            )
        }
    }
}