package com.example.motocast.ui.view.route_planner

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.route_planner.buttons.DestinationButton
import com.example.motocast.ui.view.route_planner.date_and_time.DateTimePicker
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.util.views.Header
import com.example.motocast.util.views.buttons.BasicButton
import com.example.motocast.util.views.buttons.ButtonType
import java.util.*

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
    context: Context,
    isLoading: Boolean
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

        Header(onClick = { navigateTo("home_screen") }, text = "Ruteplanlegger")
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
                        removeDestination = { removeDestination(destinationIndex) },
                        enabledRemove = destinations.size > 2 && !isLoading
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                // This is the button that adds a new destination
                BasicButton(
                    onClick = {
                        navigateTo("home_screen")
                        addDestination()
                    },
                    text = if (destinations.size < 10) "Legg til stopp " else "Maks 10 stopp",
                    enabled = destinations.size < 10
                )

                Spacer(modifier = Modifier.height(16.dp))

                DateTimePicker(
                    context = context,
                    startTime = startTime,
                    updateStartTime = { updateStartTime(it) },
                    enabled = !isLoading
                )

                // Button for clearing all destinations
                if (routesAdded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicButton(
                        buttonType = ButtonType.Transparent,
                        onClick = { clearAll() },
                        text = "Fjern alle stopp",
                        enabled = !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button for starting the route
                BasicButton(
                    onClick = { startRoute() },
                    enabled = enabledStartRoute && !isLoading,
                    content = { textStyle, dp, color, textColor ->
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = color,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Start rute",
                                style = textStyle,
                                color = color,
                                modifier = Modifier.padding(dp)
                            )
                        }
                    }

                )
            }
        })
    }
}
