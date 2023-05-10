package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.utils.buttons.BasicButton
import com.example.motocast.ui.view.utils.buttons.SettingsButton

@Composable
fun RouteAndSettingsRow(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    isLoading: Boolean = false,
    settingsNavigateTo: () -> Unit,
    buttonText: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
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