package com.example.motocast.ui.view.route_planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.motocast.util.buttons.BackButton

@Composable
fun RouteHeader(
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        BackButton(onClick = onClick)
        Text(
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = "Ruteplanlegger",
            modifier = Modifier.weight(0.8f, fill = true),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(0.1f, fill = true))
    }
}
