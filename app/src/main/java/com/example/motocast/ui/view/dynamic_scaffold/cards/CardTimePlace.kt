package com.example.motocast.ui.view.dynamic_scaffold.cards

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
import java.util.*

@Composable
fun CardTimePlace(location: String, time: Calendar?) {
    val hours = time?.get(Calendar.HOUR_OF_DAY)
    val minutes = time?.get(Calendar.MINUTE)

    Column {
        Text(text = location,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        if (hours != null && minutes != null) {
            Text(text = "$hours:$minutes")
        }
    }
}