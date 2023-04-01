package com.example.motocast.ui.view.route_planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun RoutePlannerView(
    routePlannerViewModel: RoutePlannerViewModel,
) {
    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn {
            items(routePlannerUiState.destinations.size) { destination ->
                Row {
                    Text(

                        text = when (destination) {
                            0 -> "Start"
                            routePlannerUiState.destinations.size - 1 -> "End"
                            else -> "Via"
                        },
                        modifier = Modifier
                            .padding(16.dp)

                    )

                    TextField(
                        value = routePlannerUiState.destinations[destination].name,
                        onValueChange = { name: String ->
                            routePlannerViewModel.updateDestinationName(destination, name)
                        },
                    )
                    
                    Button(
                        onClick = { routePlannerViewModel.removeDestination(destination) },
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = "Remove")
                    }
                }
            }
        }
        Button(
            onClick = { routePlannerViewModel.addDestination() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Add destination")
        }
    }
}
