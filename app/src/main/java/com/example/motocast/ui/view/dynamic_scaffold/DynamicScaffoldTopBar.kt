package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.dynamic_scaffold.badges.LocateUserBadge
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import com.example.motocast.util.views.buttons.LocateUserButton

@Composable
fun DynamicScaffoldViewTopBar(
    modifier: Modifier = Modifier,
    context: Context,
    weatherViewModel: WeatherViewModel,
    onLocateUserClick: () -> Unit,
    isTrackUserActive: Boolean,
) {
    val weatherUiState = weatherViewModel.uiState.collectAsState()
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (weatherUiState.value.temperature != null && weatherUiState.value.symbolCode != null) {
            CurrentWeatherBadge(
                context = context,
                fare = false,
                temperature =  weatherUiState.value.temperature!!.toInt(),
                iconSymbol = weatherUiState.value.symbolCode!!
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        LocateUserButton(active = isTrackUserActive) { onLocateUserClick() }
    }
}