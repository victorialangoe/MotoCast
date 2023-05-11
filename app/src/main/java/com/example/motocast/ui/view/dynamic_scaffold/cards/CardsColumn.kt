package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.motocast.data.model.Properties
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint

@Composable
fun CardsColumn(waypoints: List<RouteWithWaypoint>, context: Context, isLoading: Boolean) {

    if (isLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .size(40.dp),
            strokeWidth = 4.dp
        )

    } else {
        LazyColumn {
            items(waypoints) { waypoint ->

                val weather = waypoint.weather
                val time = waypoint.timestamp
                val temperature = weather?.temperature ?: 0
                val alerts: List<Properties> = weather?.alerts ?: listOf()
                val isInDestination = waypoint.isInDestination

                if (waypoint != waypoints.first()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = MaterialTheme.colorScheme.surface)
                            .fillMaxWidth(),
                        temperature = temperature.toInt(),
                        location = waypoint.name ?: "",
                        time = time,
                        alerts = alerts,
                        isInDestination = isInDestination,
                        iconSymbol = weather?.symbolCode ?: "",
                        context = context
                    )
            }
        }
    }
}