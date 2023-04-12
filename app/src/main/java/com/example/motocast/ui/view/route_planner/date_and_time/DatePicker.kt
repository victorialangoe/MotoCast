package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.DatePickerDialog
import android.content.Context
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
import com.example.motocast.ui.viewmodel.route_planner.DatePickerUiState
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import java.util.*

@Composable
fun DatePicker(
    routePlannerViewModel: RoutePlannerViewModel,
    context: Context = LocalContext.current
) {

    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()

// Fetching current year, month and day
    val year = routePlannerUiState.startTime.datePickerUiState.year
    val month = routePlannerUiState.startTime.datePickerUiState.month
    val dayOfMonth = routePlannerUiState.startTime.datePickerUiState.day

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            routePlannerViewModel.updateDateUiState(
                DatePickerUiState(
                    selectedYear,
                    selectedMonth,
                    selectedDayOfMonth
                )
            )
        }, year, month, dayOfMonth
    )
    datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis

    Button(
        onClick = {
            datePicker.show()
        },
        shape = RoundedCornerShape(size = 8.dp),
        colors = ButtonDefaults.buttonColors(Color(0xfff7f7f7)),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 55.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                imageVector = ImageVector.vectorResource(id = com.example.motocast.R.drawable.calendar),
                contentDescription = "Calendar icon",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                text = "${routePlannerUiState.startTime.datePickerUiState.day}. ${routePlannerUiState.startTime.datePickerUiState.month}. ${routePlannerUiState.startTime.datePickerUiState.year}",
                color = Color.Black)
        }
    }
}