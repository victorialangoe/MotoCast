package com.example.motocast.ui.view.home_bottom_scaffold

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.home_bottom_scaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.home_bottom_scaffold.favorites.FavoritesSection
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeBottomScaffoldView(
    context: Context,
    nowCastViewModel: NowCastViewModel,
    content: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier) {
            Text("Scaffold Content")
        }
    },
    onNavigateToScreen: () -> Unit
) {
    val scope = rememberCoroutineScope()
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
                    .heightIn(min = minHeight, max = maxHeight)
                    .clip(cornerShape),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CurrentWeatherRow(
                    context = context,
                    viewModel = nowCastViewModel
                )
                ContentColumn(onNavigateToScreen)
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
fun CurrentWeatherRow(context: Context, viewModel: NowCastViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentWeatherBadge(context = context, viewModel = viewModel)
        Spacer(modifier = Modifier.weight(1f))
        LocateUserBadge()
    }
}

@Composable
fun ContentColumn(onNavigateToScreen: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(cornerShape)
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.Black)
                .align(Alignment.CenterHorizontally)
        )

        AddNewRouteButton(onNavigateToScreen)

        FavoritesSection()
    }
}

// Constants
private val cornerShape = RoundedCornerShape(16.dp)
private val maxHeight = 500.dp
private val minHeight = 190.dp

// Preview
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun HomeBottomScaffoldViewPreview(
    context: Context = androidx.compose.ui.platform.LocalContext.current) {
    HomeBottomScaffoldView(context = context, nowCastViewModel = NowCastViewModel()) {
            /*modifier ->
        Box(modifier) {
            Text("Scaffold Content")
        }*/
    }
}