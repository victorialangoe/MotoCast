package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.buttons.AddNewRouteButton
import com.example.motocast.ui.view.dynamic_scaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.example.motocast.util.buttons.SettingsButton


@Composable
fun RouteScaffoldContent(
    onButtonClick: () -> Unit,
    isLoading: Boolean,
    time: String,
    date: String,
    duration: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context,
    settingsNavigateTo: () -> Unit
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                EditRouteButton(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(55.dp),
                    onNavigateToScreen = onButtonClick,
                    isLoading = isLoading
                )


                SettingsButton(
                    modifier = Modifier.height(55.dp).width(55.dp),
                    onClick = { settingsNavigateTo() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        DateTimeDurationRow(date, time, duration, isLoading)
        Spacer(modifier = Modifier.height(16.dp))
    }

    CardsColumn(waypoints = waypoints, context = context, isLoading = isLoading)
}

