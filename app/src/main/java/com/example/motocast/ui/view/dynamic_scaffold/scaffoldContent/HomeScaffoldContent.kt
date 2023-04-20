package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HomeScaffoldContent() {
    Text(
        text = "God morgen, Espen",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}
