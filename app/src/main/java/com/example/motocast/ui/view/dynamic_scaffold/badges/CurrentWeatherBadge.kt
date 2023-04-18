package com.example.motocast.ui.view.dynamic_scaffold.badges

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.cards.DisplayWeather


@Composable
fun CurrentWeatherBadge(
    temperature: Int,
    fare: Boolean = false,
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
            fare = fare,
            symbolCode = iconSymbol,
            context = context
        )
    }

}
