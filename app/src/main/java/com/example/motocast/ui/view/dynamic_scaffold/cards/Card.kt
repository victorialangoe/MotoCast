package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.data.model.Properties
import java.util.*

@Composable
fun Card(
    modifier: Modifier = Modifier,
    temperature: Int,
    isInDestination: Boolean,
    location: String,
    alerts: List<Properties>,
    time: Calendar?,
    iconSymbol: String,
    context: Context,
) {

    Column(
        modifier = modifier.padding(16.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            CardTimePlace(
                location = location,
                time = time,
                modifier = Modifier.weight(0.7f),
                isInDestination = isInDestination,
            )

            Spacer(modifier = Modifier.width(8.dp))

            DisplayWeather(
                modifier = Modifier.weight(0.3f),
                temperature = temperature,
                symbolCode = iconSymbol,
                context = context,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Alerts(alerts = alerts)
    }
}
