package com.example.motocast.ui.view.dynamicScaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun RouteText (
    routePlannerViewModel: RoutePlannerViewModel
) {

    Text(text = routePlannerViewModel.getDestinationNamesAsString(), fontSize = 20.sp)
}