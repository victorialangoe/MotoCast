package com.example.motocast.ui.view.utils.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.motocast.R

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    BasicButton(
        content = { _, contentSize, _ , _ ->
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_button_icon),
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
