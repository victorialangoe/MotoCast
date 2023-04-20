package com.example.motocast.util.views.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ButtonType {
    Filled,
    Outlined,
    White,
    Transparent,
}

enum class ButtonSize {
    Small,
    Medium,
    Large,
}

@Composable
fun BasicButton(
    text: String? = null,
    content: @Composable (
        textStyle: TextStyle,
        contentSize: Dp,
        color: Color,
    ) -> Unit = { _, _, _ -> },
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.Filled,
    buttonSize: ButtonSize = ButtonSize.Medium,
    circle: Boolean = false,
) {
    val buttonShape =MaterialTheme.shapes.extraLarge

    val buttonColors: ButtonColors = when (buttonType) {
        ButtonType.Filled -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
        ButtonType.Outlined -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        )
        ButtonType.Transparent -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        )
        ButtonType.White -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        )
    }

    val buttonHeight: Dp = when (buttonSize) {
        ButtonSize.Small -> 40.dp
        ButtonSize.Medium -> 55.dp
        ButtonSize.Large -> 70.dp
    }
    
    val textStyle = when (buttonSize) {
        ButtonSize.Small -> MaterialTheme.typography.bodySmall
        ButtonSize.Medium -> MaterialTheme.typography.bodyMedium
        ButtonSize.Large -> MaterialTheme.typography.bodyLarge
    }

    val buttonModifier = when (circle) {
        true -> modifier
            .height(buttonHeight)
            .width(buttonHeight)
        false -> modifier
            .fillMaxWidth()
            .height(buttonHeight)
    }

    val contentPadding: PaddingValues = when (buttonSize) {
        ButtonSize.Small -> PaddingValues(2.dp)
        ButtonSize.Medium -> PaddingValues(4.dp)
        ButtonSize.Large -> PaddingValues(8.dp)
    }

    val contentSize: Dp = when (buttonSize) {
        ButtonSize.Small -> 24.dp
        ButtonSize.Medium -> 32.dp
        ButtonSize.Large -> 40.dp
    }

    val contentColor = when (buttonType) {
        ButtonType.Filled -> MaterialTheme.colorScheme.onPrimary
        ButtonType.Outlined -> MaterialTheme.colorScheme.primary
        ButtonType.Transparent -> MaterialTheme.colorScheme.onSurface
        ButtonType.White -> MaterialTheme.colorScheme.onSurface
    }

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = buttonShape,
        colors = buttonColors,
        contentPadding = contentPadding,
        content = {
            if (text != null) {
                Text(
                    text = text,
                    style = textStyle,
                    modifier = Modifier.padding(contentPadding)
                )
            }
            else {
                content(
                    textStyle = textStyle,
                    contentSize = contentSize,
                    color = contentColor,
                )
            }
        },
    )

}



