package com.example.motocast.ui.view.utils.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.motocast.R

@Composable
fun CloseButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BasicButton(
        modifier = modifier,
        content = { _, contentSize, _, _ ->
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close_button_icon),
                modifier = Modifier
                    .size(contentSize)
            )
        },
        onClick = onClick,
        enabled = enabled,
        circle = true,
        buttonSize = ButtonSize.Small,
        buttonType = ButtonType.Transparent,
    )
}
