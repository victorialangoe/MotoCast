package com.example.motocast.ui.view.route_scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardTimePlace(location: String, hours: Int, minutes: Int) {
    val fHours = String.format("%02d", hours)
    val fMinutes = String.format("%02d", minutes)

    Column {
        Text(text = location,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(text = "kl $fHours:$fMinutes")
    }
}