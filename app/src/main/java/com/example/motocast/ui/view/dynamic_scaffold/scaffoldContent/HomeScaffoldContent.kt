package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.runtime.Composable
import com.example.motocast.ui.view.route_planner.buttons.RouteAndSettingsRow

@Composable
fun HomeScaffoldContent(
    onStartButtonClick: () -> Unit,
    settingsNavigateTo: () -> Unit
) {
    RouteAndSettingsRow(
        buttonText = "Lag ny rute",
        onButtonClick = onStartButtonClick,
        settingsNavigateTo = settingsNavigateTo
    )
}
