package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint

@Composable
fun DynamicScaffoldContentColum(
    modifier: Modifier = Modifier,
    routeScaffoldButtonOnClick: () -> Unit,
    homeScaffoldButtonOnClick: () -> Unit,
    routeText: String,
    showRoute: Boolean,
    date: String,
    time: String,
    waypoints: List<RouteWithWaypoint>,
    context: Context,
    routePlannerViewModel: RoutePlannerViewModel //TODO: lavere kobling
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.scaffold_dragbar),
            contentDescription = "Bar to drag scaffold up",
        )
        //Spacer (modifier = Modifier.height(8.dp))

        if (showRoute) RouteScaffoldContent(
            onButtonClick = routeScaffoldButtonOnClick,
            routeText = routeText,
            date = date,
            time = time,
            waypoints = waypoints,
            context = context,
            routePlannerViewModel = routePlannerViewModel
        )
        else HomeScaffoldContent(onButtonClick = homeScaffoldButtonOnClick)

    }
}