package com.example.motocast.ui.view.utils.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackButton(onClick: () -> Unit) {
    BasicButton(
        content = { _, contentSize, _ , _ ->
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

