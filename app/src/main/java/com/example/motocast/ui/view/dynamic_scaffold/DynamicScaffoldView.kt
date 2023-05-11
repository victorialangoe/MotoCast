package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Divider
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
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent.DynamicScaffoldContentColum
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow
import com.example.motocast.ui.view.utils.buttons.LocateUserButton
import com.example.motocast.ui.view.utils.components.Header
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicScaffoldView(
    context: Context,
    waypoints: List<RouteWithWaypoint>,
    isTrackUserActive: Boolean,
    isRouteLoading: Boolean,
    routePlannerViewModel: RoutePlannerViewModel,
    onLocateUserClick: () -> Unit,
    popBackStack: () -> Unit,
    duration: String,
    content: @Composable () -> Unit,
    onNavigateToScreen: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    // TODO: Edit this to be a dynamic scaffold
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val routeExists = routePlannerViewModel.checkIfAllDestinationsHaveNames()
    val maxHeight = if (routeExists) 700.dp else 140.dp
    val minHeight = if (routeExists) 140.dp else 140.dp
    val cornerShape = MaterialTheme.shapes.large

    LaunchedEffect(routeExists) {
        if (routeExists) {
            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
        }
    }

    Column{
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
                        .heightIn(min = minHeight, max = maxHeight),
                    verticalArrangement = Arrangement.spacedBy(8.dp),

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        // align to the right
                        horizontalAlignment = Alignment.End
                    ) {
                        LocateUserButton(active = isTrackUserActive) {
                            onLocateUserClick()
                        }
                    }
                    DynamicScaffoldContentColum(
                        modifier = Modifier
                            .clip(cornerShape.copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp)))
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
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    content()
                    // Little header at the top with a back button
                    Header(
                        modifier = Modifier
                            .padding(16.dp),
                        onClick = { popBackStack() },
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .weight(0.1f),
        ) {
            Divider(color = MaterialTheme.colorScheme.surface)

            Spacer(modifier = Modifier.height(16.dp))

            RouteAndSettingsRow(
                buttonText = stringResource(R.string.edit_route),
                onButtonClick = onNavigateToScreen,
                settingsNavigateTo = navigateToSettings,
            )
        }

    }
}