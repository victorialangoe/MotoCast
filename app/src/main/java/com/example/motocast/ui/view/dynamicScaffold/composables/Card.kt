package com.example.motocast.ui.view.dynamicScaffold.composables

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

@Composable
fun Card (temperature: Double? = null,
          symbolCode: String? = null,
          location: String? = null,
          hours: Int? = null,
          minutes: Int? = null,
          fare: Boolean = false,
          event: String? = null,
          awarenessLevel: String? = null,
          context: Context
) {

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
                hours = hours?: -1,
                minutes = minutes?: -1
            )

            Spacer(modifier = Modifier.weight(1f))

            CardWeather(temperature, fare, symbolCode, context)
        }
    }
}