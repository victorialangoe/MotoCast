package com.example.motocast.util.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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

    val buttonColor = if (enabled) Color.Black else Color.Gray
    val buttonTextColor = if (outlined) Color.Black else Color.White
    val buttonShape = RoundedCornerShape(size = 8.dp)

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
                text = text,
                fontSize = 22.sp,
                color = buttonTextColor,
                fontWeight = FontWeight.Normal
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
                contentColor = buttonTextColor,
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
                containerColor = Color.Black,
                contentColor = buttonTextColor,
            ),
            enabled = enabled,
            modifier = modifier
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun BasicButtonPreview() {
    BasicButton(
        text = "Legg til stopp",
        onClick = { },
        outlined = true,
        enabled = true,
    )
}