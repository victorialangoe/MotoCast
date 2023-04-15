package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamic_scaffold.cards.CardsColumn


@Composable
fun RouteScaffoldContent(
    onButtonClick: () -> Unit,
    routeText: String,
    time: String,
    date: String,
    duration: String,
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

    CardsColumn()
}

