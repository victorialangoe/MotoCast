package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.theme.Orange300
import com.example.motocast.ui.theme.Orange500
import com.example.motocast.ui.theme.Orange500Transparent
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint

@Composable
fun CardsColumn(waypoints: List<RouteWithWaypoint>, context: Context, isLoading: Boolean) {
    Log.d("CardsColumn", "waypoints: $waypoints")

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

                val weather = waypoint.weatherUiState
                val time = waypoint.timestamp
                val temperature = weather?.temperature ?: 0

                if (waypoint != waypoints.first()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .weight(0.9f),
                        temperature = temperature.toInt(),
                        location = waypoint.name ?: "",
                        time = time,
                        iconSymbol = weather?.symbolCode ?: "",
                        context = context
                    )
            }
        }
    }
}