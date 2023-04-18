package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint

@Composable
fun CardsColumn(waypoints: List<RouteWithWaypoint>, context: Context) {

    LazyColumn{
        items(waypoints) { waypoint ->

            val weather = waypoint.weatherUiState
            val time = waypoint.timestamp

            if (waypoint != waypoints.first()) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (weather != null) {
                Card(
                    temperature = weather.temperature?.toInt() ?: 0,
                    location = waypoint.name ?: "No name found",
                    time = time,
                    iconSymbol = weather.symbolCode ?: "",
                    context = context
                )
            }


        }
    }
}