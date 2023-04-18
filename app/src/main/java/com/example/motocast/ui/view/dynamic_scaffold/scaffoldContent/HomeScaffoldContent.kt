package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamic_scaffold.buttons.AddNewRouteButton
import com.example.motocast.util.buttons.SettingsButton

@Composable
fun HomeScaffoldContent(
    onButtonClick: () -> Unit,
    settingsNavigateTo: () -> Unit
) {

    // TODO: make a generic button for this since it's used in multiple places
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AddNewRouteButton(
                modifier = Modifier
                    .weight(0.8f)
                    .height(55.dp),
                onClick = onButtonClick
            )

                SettingsButton(
                modifier = Modifier.height(55.dp).width(55.dp),
                onClick = { settingsNavigateTo() }
            )
        }
    }
}
