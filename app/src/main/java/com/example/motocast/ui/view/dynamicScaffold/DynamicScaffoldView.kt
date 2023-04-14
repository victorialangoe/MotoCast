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
import com.example.motocast.ui.view.dynamicScaffold.composables.DynamicScaffoldTopBar
import com.example.motocast.ui.view.dynamicScaffold.scaffoldContent.DynamicScaffoldContentColumn
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
    mapLocationViewModel: MapLocationViewModel,
    content: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier) {
            Text("Scaffold Content")
        }
    },
    ScaffoldContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope() // TODO: Remove this
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = minHeight,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight, max = maxHeight),
                    //.clip(cornerShape),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DynamicScaffoldTopBar(
                    context = context,
                    nowCastViewModel = nowCastViewModel
                ) {
                    mapLocationViewModel.cameraToUserLocation()
                }
                DynamicScaffoldContentColumn(
                    ScaffoldContent = { ScaffoldContent () }
                )
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



// Constants
private val maxHeight = 700.dp
private val minHeight = 300.dp