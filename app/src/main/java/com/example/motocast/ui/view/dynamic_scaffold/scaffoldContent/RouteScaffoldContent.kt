package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint


@Composable
fun RouteScaffoldContent(
    isLoading: Boolean,
    time: String,
    date: String,
    duration: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TODO: Make this a composable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                // Material3 ProgressIndicator
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        DateTimeRow(date, time)
        Spacer(modifier = Modifier.height(16.dp))
        CardsColumn(waypoints = waypoints, context = context, isLoading = isLoading)
    }
}