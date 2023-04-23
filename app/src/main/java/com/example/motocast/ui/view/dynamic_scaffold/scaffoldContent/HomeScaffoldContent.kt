package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import java.util.Calendar

@Composable
fun HomeScaffoldContent(
    userName: String,
) {

    Text(
        text = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "God morgen, $userName"
            in 12..17 -> "God ettermiddag, $userName"
            else -> "God kveld, $userName"
        },
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}
