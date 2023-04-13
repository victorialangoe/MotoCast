package com.example.motocast.ui.view.dynamicScaffold.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardS (temperature: Int, location: String, hours: Int, minutes: Int, fare: Boolean = false) {

    Box(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(color = Color(0xfff7f7f7))
        .fillMaxWidth())
    {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)) {

            CardTimePlace(
                location = location,
                hours = hours,
                minutes = minutes
            )

            Spacer(modifier = Modifier.weight(1f))

            CardWeather(temperature, fare)
        }
    }
}