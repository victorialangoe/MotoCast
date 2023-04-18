package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint


@Composable
fun RouteScaffoldContent(
    onButtonClick: () -> Unit,
    routeText: String,
    time: String,
    date: String,
    duration: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
    ) {
        RouteText(routeText)
        Spacer(modifier = Modifier.height(16.dp))
        DateTimeDurationRow(date, time, duration)
        Spacer(modifier = Modifier.height(16.dp))
        EditRouteButton(onButtonClick)
    }

    Spacer(modifier = Modifier.height(8.dp))

    CardsColumn(waypoints = waypoints, context = context)
}

