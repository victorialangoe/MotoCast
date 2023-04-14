package com.example.motocast.ui.view.dynamicScaffold.composables

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamicScaffold.badges.CurrentWeatherBadge
import com.example.motocast.ui.view.home_bottom_scaffold.LocateUserBadge
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel

@Composable
fun DynamicScaffoldTopBar(
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