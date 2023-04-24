package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.motocast.ui.view.dynamic_scaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import com.example.motocast.ui.view.utils.buttons.LocateUserButton

@Composable
fun DynamicScaffoldViewTopBar(
    modifier: Modifier = Modifier,
    context: Context,
    weatherViewModel: CurrentWeatherViewModel,
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
                temperature =  weatherUiState.value.temperature!!.toInt(),
                iconSymbol = weatherUiState.value.symbolCode!!
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        LocateUserButton(active = isTrackUserActive) { onLocateUserClick() }
    }
}