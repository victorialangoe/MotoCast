package com.example.motocast.ui.view.utils.badges

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.motocast.ui.view.dynamic_scaffold.cards.DisplayWeather


@Composable
fun CurrentWeatherBadge(
    temperature: Int,
    iconSymbol: String,
    context: Context
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        DisplayWeather(
            temperature = temperature,
            symbolCode = iconSymbol,
            context = context
        )
    }

}
