package com.example.motocast.ui.view.route_planner

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.route_planner.buttons.AddFieldButton
import com.example.motocast.ui.view.route_planner.buttons.ClearAllButton
import com.example.motocast.ui.view.route_planner.buttons.CreateRouteButton
import com.example.motocast.ui.view.route_planner.buttons.DestinationButton
import com.example.motocast.ui.view.route_planner.date_and_time.DateTimePicker
import com.example.motocast.ui.viewmodel.route_planner.Destination
import java.util.Calendar

@Composable
fun RoutePlannerView(
    navigateTo: (screen: String) -> Unit,
    startRoute: () -> Unit,
    editDestination: (Int) -> Unit,
    addDestination: () -> Unit,
    removeDestination: (Int) -> Unit,
    updateStartTime: (Calendar) -> Unit,
    enabledStartRoute: Boolean,
    routesAdded: Boolean,
    destinations: List<Destination>,
    clearAll: () -> Unit,
    startTime: Calendar,
    context: Context
) {

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RouteHeader(onClick = { navigateTo("home_screen") })
        // This makes the content scrollable
        LazyColumn(content = {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                // This is where all the added destinations will be shown (if any)
                destinations.forEachIndexed { destinationIndex, _ ->
                    Spacer(modifier = Modifier.height(8.dp)) // Add a gap between the items
                    DestinationButton(
                        destinationIndex = destinationIndex,
                        destinations = destinations,
                        editDestination = { editDestination(destinationIndex) },
                        removeDestination = { removeDestination(destinationIndex) }
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                AddFieldButton(
                    addDestination = { addDestination() },
                    enabled = destinations.size < 10
                )

                Spacer(modifier = Modifier.height(16.dp))

                DateTimePicker(
                    context = context,
                    startTime = startTime,
                    updateStartTime = { updateStartTime(it) }
                )

                if (routesAdded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ClearAllButton(clearAll = { clearAll() })
                }

                Spacer(modifier = Modifier.height(16.dp))

                CreateRouteButton(
                    startRoute = { startRoute() },
                    enabled = enabledStartRoute
                )


            }
        })
    }
}
