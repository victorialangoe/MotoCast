package com.example.motocast.util.views.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(onClick: () -> Unit) {
    BasicButton(
        content = { _, contentSize ->
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(contentSize)
            )
        },
        onClick = onClick,
        circle = true,
        buttonSize = ButtonSize.Small,
        buttonType = ButtonType.Transparent,
    )
}

