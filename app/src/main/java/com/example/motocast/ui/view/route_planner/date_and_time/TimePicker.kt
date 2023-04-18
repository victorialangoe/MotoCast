package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.*
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DateAndTimeButton
import com.example.motocast.ui.viewmodel.route_planner.TimePickerUiState

@Composable
fun TimePicker(
    context: Context,
    modifier: Modifier = Modifier,
    updateTimeUiState: (TimePickerUiState) -> Unit,
    hour: Int,
    minute: Int,
) {
    val timePicker = remember {
        TimePickerDialog(context, { _, h, m ->
            updateTimeUiState(TimePickerUiState(h, m))
        }, hour, minute, true)
    }

    var label = if (minute < 10) {
        "${hour}:0${minute}"
    } else {
        "${hour}:${minute}"
    }
    // If the time is now, show "Nå" instead of the time
    val calendar = Calendar.getInstance()
    if (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) == minute) {
        label = "Nå"
    }

    DateAndTimeButton(
        modifier = modifier,
        label = label,
        onClick = { timePicker.show() },
        icon = R.drawable.clock,
        iconDescription = "Klokke icon",
    )
}