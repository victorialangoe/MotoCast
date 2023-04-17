package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun Card(
    temperature: Int,
    location: String,
    time: Calendar?,
    fare: Boolean = false,
    iconSymbol: String,
    context: Context,
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xfff7f7f7))
            .fillMaxWidth()
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)
        ) {

            CardTimePlace(
                location = location,
                time = time,
            )

            Spacer(modifier = Modifier.weight(1f))

            DisplayWeather(temperature, fare, iconSymbol, context = context)
        }
    }
}