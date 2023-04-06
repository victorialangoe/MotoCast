package com.example.motocast.ui.view.inputs

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R


@Composable
fun TimePicker() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedTimeText by remember { mutableStateOf("") }

// Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            selectedTimeText = "$selectedHour:$selectedMinute"
        }, hour, minute, false
    )

    Button(
        onClick = {
        timePicker.show()
    },
        shape = RoundedCornerShape(size = 8.dp),
        colors = ButtonDefaults.buttonColors(Color(0xfff7f7f7)),
        modifier = Modifier
            .width(width = 160.dp)
            .height(height = 55.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.clock),
                contentDescription = "Clock icon",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                text = selectedTimeText.ifEmpty {
                    "$hour:$minute"
                },
                color = Color.Black)
        }
    }
}
