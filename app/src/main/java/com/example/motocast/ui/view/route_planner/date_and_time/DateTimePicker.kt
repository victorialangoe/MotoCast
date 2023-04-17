package com.example.motocast.ui.view.route_planner.date_and_time

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.route_planner.DatePickerUiState
import com.example.motocast.ui.viewmodel.route_planner.TimePickerUiState

@Composable
fun DateTimePicker(
    context: Context,
    updateDateUiState: (DatePickerUiState) -> Unit,
    updateTimeUiState: (TimePickerUiState) -> Unit,
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DatePicker(
            context = context,
            updateDateUiState = updateDateUiState,
            year = year,
            month = month,
            day = day,
            modifier = Modifier.weight(1f, true)
        )
        Spacer(modifier = Modifier.width(16.dp))
        TimePicker(
            context = context,
            updateTimeUiState = updateTimeUiState,
            hour = hour,
            minute = minute,
            modifier = Modifier.weight(1f, true)
        )
    }
}

