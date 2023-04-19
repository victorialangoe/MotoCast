package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.view.dynamic_scaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.example.motocast.util.badges.BasicBadge
import com.example.motocast.R


@Composable
fun RouteScaffoldContent(
    onButtonClick: () -> Unit,
    routeText: String,
    time: String,
    date: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context,
    routePlannerViewModel: RoutePlannerViewModel // TODO: lavere kobling
) {
    Column(
        modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = routePlannerViewModel.getDuration(), textAlign = TextAlign.Center, fontSize = 20.sp)
        
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.line_separator),
            contentDescription = "Line to separate content in scaffold",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateTimeDurationRow(date, time)

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.line_separator),
            contentDescription = "Line to separate content in scaffold",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        RouteText(routeText)

        Spacer(modifier = Modifier.height(16.dp))

        CardsColumn(waypoints = waypoints, context = context)

        Spacer(modifier = Modifier.height(16.dp))

        EditRouteButton(onButtonClick)

    }
}

