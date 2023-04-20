package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.util.views.buttons.BasicButton
import com.example.motocast.util.views.buttons.SettingsButton

@Composable
fun RouteAndSettingsRow(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    isLoading: Boolean = false,
    settingsNavigateTo: () -> Unit,
    buttonText: String = "Endre rute"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicButton(
            modifier = Modifier.weight(0.2f),
            onClick = { onButtonClick() },
            text = buttonText,
            enabled = !isLoading,
        )

        Spacer(modifier = Modifier.width(8.dp))

        SettingsButton(
            modifier = Modifier.weight(0.1f),
            onClick = { settingsNavigateTo() }
        )
    }
}