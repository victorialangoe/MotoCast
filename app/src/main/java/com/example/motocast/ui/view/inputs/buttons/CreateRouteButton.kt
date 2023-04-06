package com.example.motocast.ui.view.inputs.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateRouteButton(onNavigateToScreen: () -> Unit) {
    Button(
        onClick = onNavigateToScreen,
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .width(width = 340.dp)
            .height(height = 55.dp),
        colors = ButtonDefaults.buttonColors(Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ferdig",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }
    }
}