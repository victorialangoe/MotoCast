package com.example.motocast.ui.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.route_planner.Header

@Composable
fun SettingsView(
    popBackStack: () -> Unit,
    setDarkMode: (Boolean) -> Unit,
    darkMode: Boolean,
) {

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header(onClick = { popBackStack() }, text = "Innstillinger")

        Spacer(modifier = Modifier.padding(16.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            ChooseScreenMode(
                darkMode = darkMode,
                setDarkMode = setDarkMode
            )
        }
    }
}
