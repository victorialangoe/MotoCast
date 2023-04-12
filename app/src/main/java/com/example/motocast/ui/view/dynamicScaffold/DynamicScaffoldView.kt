package com.example.motocast.ui.view.dynamicScaffold

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamicScaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.dynamicScaffold.scaffoldContent.HomeScaffoldContent
import com.example.motocast.ui.view.dynamicScaffold.scaffoldContent.RouteScaffoldContent
import com.example.motocast.ui.view.home_bottom_scaffold.*
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicScaffoldView(
    context: Context,
    nowCastViewModel: NowCastViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    mapLocationViewModel: MapLocationViewModel,
    content: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier) {
            Text("Scaffold Content")
        }
    },
    onNavigateToScreen: () -> Unit
) {
    val scope = rememberCoroutineScope() // TODO: Remove this
    val scaffoldState = rememberBottomSheetScaffoldState() // Endre etterpå

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
                    nowCastViewModel = nowCastViewModel
                ) {
                    mapLocationViewModel.cameraToUserLocation()
                }
                ContentColumn(onNavigateToScreen, routePlannerViewModel)
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            ) {
                content(Modifier)
            }
        }
    )
}

@Composable
fun DynamicScaffoldViewTopBar(
    context: Context,
    nowCastViewModel: NowCastViewModel,
    cameraToUserLocation: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentWeatherBadge(context = context, nowCastViewModel = nowCastViewModel)
        Spacer(modifier = Modifier.weight(1f))
        LocateUserBadge(cameraToUserLocation = cameraToUserLocation)
    }
}

@Composable
fun ContentColumn(
    onNavigateToScreen: () -> Unit,
    routePlannerViewModel: RoutePlannerViewModel)
{

    Column(
        modifier = Modifier
            .clip(cornerShape)
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.scaffold_dragbar),
            contentDescription = "Bar to drag scaffold up",
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )
        if (routePlannerViewModel.checkIfAllDestinationsHaveNames()) {
            RouteScaffoldContent(onNavigateToScreen, routePlannerViewModel)
            }
        else {
            HomeScaffoldContent(onNavigateToScreen)
        }
    }
}



// Constants
private val cornerShape = RoundedCornerShape(16.dp)
private val maxHeight = 700.dp
private val minHeight = 300.dp