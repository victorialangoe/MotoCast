package com.example.motocast.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.utils.components.TransparentTopBar
import com.example.motocast.ui.view.utils.buttons.BasicButton
import com.example.motocast.ui.view.utils.buttons.ButtonSize
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel

@Composable
fun HomeView(
    context: Context,
    weatherViewModel: CurrentWeatherViewModel,
    settingsNavigateTo: () -> Unit,
    onCreateNewRouteClick: () -> Unit,
    onLocateUserClick: () -> Unit,
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
                onSettingsClick = settingsNavigateTo,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCreateNewRouteClick() },
                buttonSize = ButtonSize.Large,
                text = stringResource(id = R.string.make_new_route),
            )
        }
    }
}