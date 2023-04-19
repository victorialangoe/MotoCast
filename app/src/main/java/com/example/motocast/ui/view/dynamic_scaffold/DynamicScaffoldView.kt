package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent.DynamicScaffoldContentColum
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicScaffoldView(
    context: Context,
    destinations: List<Destination>,
    waypoints: List<RouteWithWaypoint>,
    isTrackUserActive: Boolean,
    weatherViewModel: WeatherViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    mapLocationViewModel: MapLocationViewModel,
    content: @Composable (Modifier) -> Unit,
    onNavigateToScreen: () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val routeExists = routePlannerViewModel.checkIfAllDestinationsHaveNames()
    val cornerShape = RoundedCornerShape(16.dp) //TODO: få til kun runde på toppen og ikke bunnen?
    val maxHeight = if (routeExists) 1000.dp else 160.dp
    val minHeight = if (routeExists) 140.dp else 160.dp

    LaunchedEffect(routeExists){
        if (routeExists) {coroutineScope.launch { scaffoldState.bottomSheetState.expand() }}
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = minHeight,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight, max = maxHeight)
                    .clip(cornerShape),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                DynamicScaffoldViewTopBar(
                    context = context,
                    weatherViewModel = weatherViewModel,
                    onLocateUserClick = {

                        mapLocationViewModel.trackUserOnMap(
                            routeExists = routeExists,
                            destinations = destinations
                        )
                    },
                    isTrackUserActive = isTrackUserActive
                )

                DynamicScaffoldContentColum(
                    modifier = Modifier
                        .clip(cornerShape)
                        .background(Color.White, shape = cornerShape)
                        .border(border = BorderStroke(1.dp, Color.LightGray), shape = cornerShape)
                        .fillMaxSize()
                        .padding(16.dp),
                    homeScaffoldButtonOnClick = onNavigateToScreen,
                    routeScaffoldButtonOnClick = onNavigateToScreen,
                    routeText = routePlannerViewModel.getDestinationNamesAsString(),
                    showRoute = routeExists,
                    date = routePlannerViewModel.getStartDate(),
                    time = routePlannerViewModel.getStartTime(),
                    waypoints = waypoints,
                    context = context,
                    routePlannerViewModel = routePlannerViewModel
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                content(Modifier)
            }
        }
    )
}