package com.example.motocast.ui.view.dynamicScaffold.scaffoldContent

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.motocast.ui.view.dynamicScaffold.composables.RouteText
import com.example.motocast.ui.view.dynamicScaffold.buttons.EditRouteButton
import com.example.motocast.ui.view.dynamicScaffold.composables.CardsColumn
import com.example.motocast.ui.view.dynamicScaffold.composables.DateAndTimeRow
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel


@Composable
fun RouteScaffoldContent(
    onNavigateToScreen: () -> Unit,
    routeText: String,
    destinations: List<Destination>,
    month: Int,
    day: Int,
    hours: Int,
    minutes: Int,
    nowCastViewModel: NowCastViewModel,
    context: Context
    ) {

    RouteText(routeText)

    DateAndTimeRow(month, day, hours, minutes)

    EditRouteButton(onNavigateToScreen)

    CardsColumn(destinations, nowCastViewModel, context)
}