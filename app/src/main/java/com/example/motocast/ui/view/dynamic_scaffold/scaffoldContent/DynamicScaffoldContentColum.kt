package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint

@Composable
fun DynamicScaffoldContentColum(
    modifier: Modifier = Modifier,
    isRouteLoading: Boolean,
    showScroll: Boolean = true,
    showRoute: Boolean,
    date: String,
    time: String,
    duration: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context
) {

    Column(
        modifier = modifier
            .fillMaxHeight(),

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        if (showScroll) {
            // Little box
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(MaterialTheme.shapes.extraLarge)

                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (showRoute) RouteScaffoldContent(
            isLoading = isRouteLoading,
            date = date,
            time = time,
            duration = duration,
            waypoints = waypoints,
            context = context,
        )
    }
}