package com.example.motocast.util.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation? = null,
    shape: CornerBasedShape = MaterialTheme.shapes.extraLarge,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    ),
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
) {
    androidx.compose.material3.OutlinedButton(
        onClick = { onClick() },
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
    }
}
