package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun RouteText(
    text: String,
) {

    Text(
        text = text,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyMedium
    )
}