package com.example.motocast.ui.view.dynamic_scaffold

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.dynamic_scaffold.badges.LocateUserBadge
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel

@Composable
fun DynamicScaffoldViewTopBar(
    context: Context,
    weatherViewModel: WeatherViewModel,
    onLocateUserClick: () -> Unit,
    isTrackUserActive: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentWeatherBadge(context = context, weatherViewModel = weatherViewModel)
        Spacer(modifier = Modifier.weight(1f))
        LocateUserBadge(onLocateUserClick = { onLocateUserClick() }, active = isTrackUserActive)
    }
}