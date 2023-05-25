package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent.DynamicScaffoldContentColum
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow
import com.example.motocast.ui.view.utils.buttons.LocateUserButton
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import kotlinx.coroutines.launch

// Here we are using a ExperimentalMaterialApi annotation to suppress the warning, since the
// BottomSheetScaffold is experimental.
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicScaffoldView(
    context: Context,
    waypoints: List<RouteWithWaypoint>,
    isTrackUserActive: Boolean,
    isRouteLoading: Boolean,
    routePlannerViewModel: RoutePlannerViewModel,
    onLocateUserClick: () -> Unit,
    duration: String,
    content: @Composable () -> Unit,
    onNavigateToScreen: () -> Unit,
    navigateToSettings: () -> Unit,
    maxHeight: Dp = 500.dp,
    minHeight: Dp = 0.dp,
    editRoute: Boolean = false,
    header: @Composable () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val routeExists = routePlannerViewModel.checkIfAllDestinationsHaveNames()
    val cornerShape = MaterialTheme.shapes.large

    LaunchedEffect(routeExists) {
        if (routeExists) {
            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
        }
    }

    Column {
        BottomSheetScaffold(
            modifier = if (routeExists) Modifier.weight(0.9f) else Modifier.weight(1f),
            scaffoldState = scaffoldState,
            sheetPeekHeight = minHeight,
            sheetBackgroundColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            sheetShape = cornerShape,
            contentColor = Color.Transparent,
            sheetElevation = 0.dp,
            sheetContent = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight, max = maxHeight),
                    verticalArrangement = Arrangement.spacedBy(8.dp),

                    ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        LocateUserButton(active = isTrackUserActive) {
                            onLocateUserClick()
                        }
                    }
                    DynamicScaffoldContentColum(
                        modifier = Modifier
                            .clip(
                                cornerShape.copy(
                                    bottomEnd = CornerSize(0.dp),
                                    bottomStart = CornerSize(0.dp)
                                )
                            )
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
                        showScroll = routeExists,
                    )

                }
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    content()
                    if (routeExists) {
                        header()
                    }
                }
            }
        )

        if (editRoute) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
                    .weight(0.1f),
            ) {
                RouteAndSettingsRow(
                    buttonText = stringResource(R.string.edit_route),
                    onButtonClick = onNavigateToScreen,
                    settingsNavigateTo = navigateToSettings,
                )
            }

        }
    }
}