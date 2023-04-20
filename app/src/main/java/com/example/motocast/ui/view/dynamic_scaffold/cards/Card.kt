package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.theme.red700
import com.example.motocast.ui.theme.red900
import java.util.*

@Composable
fun Card(
    modifier: Modifier = Modifier,
    temperature: Int,
    location: String,
    alert: String?,
    time: Calendar?,
    fare: Boolean = false,
    iconSymbol: String,
    context: Context,
) {

    Column(
        modifier = modifier
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {

            CardTimePlace(
                location = location,
                time = time,
                modifier = Modifier.weight(0.7f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            DisplayWeather(
                modifier = Modifier.weight(0.3f),
                temperature = temperature,
                fare = fare,
                symbolCode = iconSymbol,
                context = context,
            )
        }

        if (alert != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)

                    .background(
                        color = isSystemInDarkTheme().let {
                            if (it) {
                                red700// Make a theme for this
                            } else {
                                red900
                            }
                        }
                    )
            ) {
                Text(
                    color = MaterialTheme.colorScheme.background,
                    text = alert,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}