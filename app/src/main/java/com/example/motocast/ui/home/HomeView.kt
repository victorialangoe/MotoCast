package com.example.motocast.ui.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamic_scaffold.TransparentTopBar
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow
import com.example.motocast.ui.view.settings.ChooseScreenMode
import com.example.motocast.ui.view.settings.ChooseUserName
import com.example.motocast.ui.view.utils.buttons.BasicButton
import com.example.motocast.ui.view.utils.components.Header
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel

@Composable
fun HomeView(
    context: Context,
    weatherViewModel: CurrentWeatherViewModel,
    settingsNavigateTo: () -> Unit,
    onCreateNewRouteClick: () -> Unit,
    onLocateUserClick: () -> Unit,
    onEditRouteClick: () -> Unit,
    routeExists: Boolean,
    isTrackUserActive: Boolean,
    mapView: @Composable () -> Unit,
) {
    // Map as background, buttons on bottom of screen

    Box(modifier = Modifier.fillMaxSize()) {
        mapView()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            TransparentTopBar(
                context =  context,
                weatherViewModel = weatherViewModel,
                onLocateUserClick = onLocateUserClick,
                isTrackUserActive = isTrackUserActive,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if(routeExists){
                BasicButton(
                    text = stringResource(id = R.string.edit_route),
                    onClick = onEditRouteClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            RouteAndSettingsRow(
                onButtonClick = onCreateNewRouteClick,
                settingsNavigateTo = settingsNavigateTo,
                buttonText = stringResource(id = R.string.make_new_route),
            )
        }
    }
}