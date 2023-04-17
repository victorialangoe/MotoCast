package com.example.motocast.util.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicButton(
    text: String,
    modifier: Modifier = Modifier,
    centerContent: Boolean = true,
    onClick: () -> Unit,
    outlined: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {

    val buttonColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    var buttonTextColor = if (outlined) buttonColor else MaterialTheme.colorScheme.onPrimary
    val buttonShape = MaterialTheme.shapes.extraLarge

    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (centerContent) Arrangement.Center else Arrangement.Start
        ) {
            leadingIcon?.let {
                it()
                Spacer(modifier = Modifier.width(20.dp))
            }
            Text(
                text = if (text.length > 25 && !centerContent) text.substring(0, 25) + "..." else text,
                style = MaterialTheme.typography.labelMedium,
                color = buttonTextColor,
            )
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(20.dp))
                it()
            }
        }
    }

    if (outlined) {
        OutlinedButton(
            onClick = { onClick() },
            shape = buttonShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, buttonColor),
            enabled = enabled,
            modifier = modifier
        ) {
            content()
        }
    } else {
        Button(
            onClick = { onClick() },
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor,
            ),
            enabled = enabled,
            modifier = modifier
        ) {
            content()
        }
    }
}