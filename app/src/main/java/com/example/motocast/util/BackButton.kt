package com.example.motocast.util

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun BackButton(onClick: () -> Unit) {
    TextButton(onClick = onClick,
        shape = CircleShape,

    ) {
        Text(
            text = "Tilbake",
            fontSize = 20.sp,
        )

    }
}

@Preview
@Composable
fun BackButtonPreview() {
    BackButton(onClick = {})
}
