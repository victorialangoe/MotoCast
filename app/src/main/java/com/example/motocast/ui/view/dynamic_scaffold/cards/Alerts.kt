package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.data.model.Properties
import com.example.motocast.theme.*
import com.example.motocast.ui.view.getWeatherIcon

@Composable
fun Alerts(
    alerts: List<Properties>,
) {
    if (alerts.isNotEmpty()) {
        alerts.map { alert ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = when (alert.awareness_level) {
                            "2; yellow; Moderate" -> if (isSystemInDarkTheme()) Yellow100 else Yellow700Transparent
                            "3; orange; Severe" -> if (isSystemInDarkTheme()) Orange100 else Orange500Transparent
                            else -> if (isSystemInDarkTheme()) Red100 else Red700Transparent
                        },
                        shape = MaterialTheme.shapes.medium,
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = getWeatherIcon(
                                alert.event,
                                alert.awareness_level
                            )
                        ),
                        contentDescription = stringResource(R.string.alert_icon),
                        modifier = Modifier
                            .padding(16.dp)
                    )

                    Text(
                        text = alert.eventAwarenessName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.info_icon),
                    tint = when (alert.awareness_level) {
                        "2; yellow; Moderate" -> if (isSystemInDarkTheme()) Yellow700 else MaterialTheme.colorScheme.onSurface
                        "3; orange; Severe" -> if (isSystemInDarkTheme()) Orange700 else MaterialTheme.colorScheme.onSurface
                        else -> if (isSystemInDarkTheme()) Red700 else MaterialTheme.colorScheme.surface
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}