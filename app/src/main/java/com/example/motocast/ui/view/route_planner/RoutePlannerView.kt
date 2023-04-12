package com.example.motocast.ui.view.route_planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.AddFieldButton
import com.example.motocast.ui.view.route_planner.buttons.ClearAllButton
import com.example.motocast.ui.view.route_planner.buttons.CreateRouteButton
import com.example.motocast.ui.view.route_planner.buttons.DestinationButton
import com.example.motocast.ui.view.route_planner.date_and_time.DatePicker
import com.example.motocast.ui.view.route_planner.date_and_time.TimePicker
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun RoutePlannerView(
    routePlannerViewModel: RoutePlannerViewModel,
    navController: NavController,
) {
    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RouteHeader(
            navController = navController,
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(routePlannerUiState.destinations.size) { destinationIndex ->
                DestinationButton(
                    header = when (destinationIndex) {
                        0 -> "Fra"
                        routePlannerUiState.destinations.size - 1 -> "Til"
                        else -> "Via"
                    },
                    text = when (destinationIndex) {
                        // TODO: We need to use the string resources!!!
                        0 -> routePlannerUiState.destinations[destinationIndex].name
                            ?: "Legg til startadresse"
                        routePlannerUiState.destinations.size - 1 -> routePlannerUiState.destinations[destinationIndex].name ?: "Legg til sluttpunkt"
                        else -> routePlannerUiState.destinations[destinationIndex].name ?: "Legg til via-adresse"
                    },
                    description = when (destinationIndex) {
                        0 -> routePlannerUiState.destinations[destinationIndex].name
                            ?: "Inputfelt for startadresse"
                        routePlannerUiState.destinations.size - 1 -> routePlannerUiState.destinations[destinationIndex].name ?: "Inputfelt for sluttpunkt"
                        else -> routePlannerUiState.destinations[destinationIndex].name ?: "Inputfelt for via-adresse"
                    },
                    icon = when (destinationIndex) {
                        0 -> R.drawable.line_end_arrow_rounded
                        routePlannerUiState.destinations.size - 1 -> R.drawable.mdi_goal
                        else -> R.drawable.format_line_spacing_rounded
                    },
                    editAddress = {
                        navController.navigate("add_destination_screen")
                        routePlannerViewModel.setActiveDestinationIndex(destinationIndex)
                    },
                    removeFromRoute = {
                        routePlannerViewModel.removeDestination(destinationIndex)
                    },
                    removeable = routePlannerUiState.destinations.size > 2
                )
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        AddFieldButton(routePlannerViewModel = routePlannerViewModel, navController = navController)

        Spacer(modifier = Modifier.height(20.dp))

        // TODO: Make this a custom component
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .padding(8.dp)
            ) {
                DatePicker(routePlannerViewModel)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .padding(8.dp)
            ) {
                TimePicker(routePlannerViewModel)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (routePlannerUiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = routePlannerUiState.error ?: "",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        CreateRouteButton(
            navController = navController,
            routePlannerViewModel = routePlannerViewModel
        )

        Spacer(modifier = Modifier.height(20.dp))

        ClearAllButton(routePlannerViewModel = routePlannerViewModel)
    }
}
