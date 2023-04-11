package com.example.motocast.ui.view.dynamicScaffold.scaffoldContent

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamicScaffold.RouteText
import com.example.motocast.ui.view.dynamicScaffold.badges.DateBadge
import com.example.motocast.ui.view.dynamicScaffold.badges.TimeBadge
import com.example.motocast.ui.view.dynamicScaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamicScaffold.cards.CardsColumn
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel


@Composable
fun RouteScaffoldContent(
    onNavigateToScreen: () -> Unit,
    routePlannerViewModel: RoutePlannerViewModel
) {
    RouteText(routePlannerViewModel)

    DateAndTimeRow()

    EditRouteButton(onNavigateToScreen)

    CardsColumn()
}

@Composable
fun DateAndTimeRow(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        TimeBadge()
        Spacer(modifier = Modifier.width(50.dp))
        DateBadge()
    }
}