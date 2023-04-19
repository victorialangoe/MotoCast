package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.example.motocast.util.views.buttons.BasicButton
import com.example.motocast.util.views.buttons.SettingsButton


@Composable
fun RouteScaffoldContent(
    onEditButtonClick: () -> Unit,
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
        RouteAndSettingsRow(
            onButtonClick = onEditButtonClick,
            isLoading = isLoading,
            settingsNavigateTo = settingsNavigateTo
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateTimeDurationRow(date, time, duration, isLoading)
        Spacer(modifier = Modifier.height(16.dp))
    }

    CardsColumn(waypoints = waypoints, context = context, isLoading = isLoading)
}

