package com.example.motocast.ui.view.route_planner

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DestinationButton
import com.example.motocast.ui.view.route_planner.date_and_time.DateTimePicker
import com.example.motocast.ui.view.utils.buttons.BasicButton
import com.example.motocast.ui.view.utils.buttons.ButtonType
import com.example.motocast.ui.view.utils.components.Header
import com.example.motocast.ui.viewmodel.route_planner.Destination
import java.util.*

@Composable
fun RoutePlannerView(
    startRoute: () -> Unit,
    editDestination: (Int) -> Unit,
    addDestination: () -> Unit,
    removeDestination: (Int) -> Unit,
    updateStartTime: (Calendar) -> Unit,
    enabledStartRoute: Boolean,
    popBackStack: () -> Unit,
    routesAdded: Boolean,
    destinations: List<Destination>,
    clearAll: () -> Unit,
    startTime: Calendar,
    context: Context,
    isLoading: Boolean,
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

        Header(
            onClick = { popBackStack() },
            text = stringResource(R.string.route_planner)
        )

        LazyColumn(content = {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                destinations.forEachIndexed { destinationIndex, _ ->
                    Spacer(modifier = Modifier.height(8.dp))
                    DestinationButton(
                        destinationIndex = destinationIndex,
                        destinations = destinations,
                        editDestination = {
                            editDestination(destinationIndex)
                        },
                        removeDestination = { removeDestination(destinationIndex) },
                        enabledRemove = destinations.size > 2 && !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BasicButton(
                    onClick = { addDestination() },
                    text = if (destinations.size < 10) stringResource(R.string.add_stopp)
                    else stringResource(R.string.max_stops_reached),
                    enabled = destinations.size < 10
                )

                Spacer(modifier = Modifier.height(16.dp))

                DateTimePicker(
                    context = context,
                    startTime = startTime,
                    updateStartTime = { updateStartTime(it) },
                    enabled = !isLoading
                )

                if (routesAdded) {

                    Spacer(modifier = Modifier.height(16.dp))

                    BasicButton(
                        buttonType = ButtonType.Transparent,
                        onClick = { clearAll() },
                        text = stringResource(R.string.remove_all_stops),
                        enabled = !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BasicButton(
                    onClick = { startRoute() },
                    text = if (isLoading) null else stringResource(R.string.start_route),
                    enabled = enabledStartRoute && !isLoading,
                    content = { _, _, color, _ ->
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = color,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
        })
    }
}

