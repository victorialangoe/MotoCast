package com.example.motocast.ui.view.utils.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.utils.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.utils.buttons.LocateUserButton
import com.example.motocast.ui.view.utils.buttons.SettingsButton
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel

@Composable
fun TransparentTopBar(
    modifier: Modifier = Modifier,
    context: Context,
    weatherViewModel: CurrentWeatherViewModel,
    onLocateUserClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isTrackUserActive: Boolean,
) {
    val weatherUiState = weatherViewModel.uiState.collectAsState()
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {

        if (weatherUiState.value.temperature != null && weatherUiState.value.symbolCode != null) {
            Box(modifier = Modifier
                .height(56.dp)
            ) {
                CurrentWeatherBadge(
                    context = context,
                    temperature =  weatherUiState.value.temperature!!.toInt(),
                    iconSymbol = weatherUiState.value.symbolCode!!
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                // round corners
                .clip(MaterialTheme.shapes.extraLarge)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.End
        ) {
            LocateUserButton(active = isTrackUserActive) { onLocateUserClick() }
            Box(modifier = Modifier
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )

            SettingsButton { onSettingsClick() }
        }
    }
}