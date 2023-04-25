package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent.DynamicScaffoldContentColum
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
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
    isRouteLoading: Boolean,
    weatherViewModel: CurrentWeatherViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    userName: String,
    mapViewModel: MapViewModel,
    duration: String,
    content: @Composable (Modifier) -> Unit,
    onNavigateToScreen: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    // TODO: Edit this to be a dynamic scaffold
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val routeExists = routePlannerViewModel.checkIfAllDestinationsHaveNames()
    val maxHeight = if (routeExists) 800.dp else 140.dp
    val minHeight = if (routeExists) 117.dp else 140.dp
    val cornerShape = MaterialTheme.shapes.large

    LaunchedEffect(routeExists) {
        if (routeExists) {
            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
        }
    }

    Column {
        BottomSheetScaffold(
            modifier = Modifier
                .background(color = Color.Red)
                .weight(0.9f),
            scaffoldState = scaffoldState,
            sheetPeekHeight = minHeight,
            sheetBackgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
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
                        modifier = Modifier.padding(horizontal = 16.dp),
                        context = context,
                        weatherViewModel = weatherViewModel,
                        onLocateUserClick = {

                            mapViewModel.trackUserOnMap(
                                routeExists = routeExists,
                                destinations = destinations
                            )
                        },
                        isTrackUserActive = isTrackUserActive
                    )

                    DynamicScaffoldContentColum(
                        modifier = Modifier
                            .clip(cornerShape)
                            .background(color = MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .padding(16.dp),
                        isRouteLoading = isRouteLoading,
                        showRoute = routeExists,
                        date = routePlannerViewModel.getStartDate(),
                        time = routePlannerViewModel.getStartTime(),
                        duration = duration,
                        waypoints = waypoints,
                        context = context,
                        userName = userName,
                        showScroll = routeExists,
                    )

                }
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    content(Modifier)
                }
            }
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(0.1f),
        ) {
            Divider(color = MaterialTheme.colorScheme.surface)

            RouteAndSettingsRow(
                buttonText = if (routeExists) stringResource(R.string.edit_route) else stringResource(
                    R.string.make_new_route
                ),
                onButtonClick = onNavigateToScreen,
                settingsNavigateTo = navigateToSettings,
            )
        }

    }
}