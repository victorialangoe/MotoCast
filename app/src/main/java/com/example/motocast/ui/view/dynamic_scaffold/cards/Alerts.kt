package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val (selectedAlert, setSelectedAlert) = remember { mutableStateOf<Properties?>(null) }
    val (isDialogVisible, setDialogVisible) = remember { mutableStateOf(false) }

    if (alerts.isNotEmpty()) {
        alerts.map { alert ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setSelectedAlert(alert)
                        setDialogVisible(true)
                    }
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

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { setDialogVisible(false) },
            title = {
                Text(
                    text = selectedAlert?.eventAwarenessName ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface

                )
            },
            text = {
                Column {
                    Text(
                        text = selectedAlert?.description?.replace("Alert: ", "") ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = stringResource(R.string.recommendations),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface

                    )
                    // split the instructions on . selectedAlert?.instruction?
                    BulletPoints(selectedAlert?.instruction ?: "")
                }
            },
            confirmButton = {
                TextButton(onClick = { setDialogVisible(false) }) {
                    Text(
                        text = stringResource(R.string.close),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
        )
    }

}