package com.example.motocast.ui.view.inputs

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun DatePicker() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }

// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )
    datePicker.datePicker.minDate = calendar.timeInMillis

    Button(
        onClick = {
            datePicker.show()
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
                imageVector = ImageVector.vectorResource(id = com.example.motocast.R.drawable.calendar),
                contentDescription = "Calendar icon",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                text = selectedDateText.ifEmpty {
                    "$dayOfMonth.$month.$year"
                },
                color = Color.Black)
        }
    }
}