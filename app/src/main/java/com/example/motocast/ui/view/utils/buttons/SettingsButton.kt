package com.example.motocast.ui.view.utils.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    BasicButton(
        content = { _, contentSize, _ , _ ->
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Innstillinger Ikon",
                modifier = modifier
                    .size(contentSize)
            )
        },
        onClick = onClick,
        circle = true,
        buttonSize = ButtonSize.Small,
        buttonType = ButtonType.Transparent,
    )
}
