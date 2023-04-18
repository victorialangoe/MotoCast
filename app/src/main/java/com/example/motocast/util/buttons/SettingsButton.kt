package com.example.motocast.util.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(Color.Transparent, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = androidx.compose.ui.Alignment.Center

    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Innstillinge icon",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(8.dp)
        )
    }
}