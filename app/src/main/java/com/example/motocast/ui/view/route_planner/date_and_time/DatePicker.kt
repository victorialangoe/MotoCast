package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.DatePickerUiState
import java.util.*


@Composable
fun DatePicker(
    context: Context,
    modifier: Modifier = Modifier,
    updateDateUiState: (DatePickerUiState) -> Unit,
    year: Int,
    month: Int,
    day: Int,
) {
    val datePicker = remember { DatePickerDialog(context) }

    datePicker.updateDate(year, month, day)
    datePicker.setOnDateSetListener { _, y, m, d ->
        updateDateUiState(DatePickerUiState(y, m, d))
    }

    datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis

    var label = "${day}. ${month+1}. $year"

    // If the time is now, show "I dag" instead of the date
    val calendar = Calendar.getInstance()
    if (
        calendar.get(Calendar.YEAR) == year &&
        calendar.get(Calendar.MONTH) == month &&
        calendar.get(Calendar.DAY_OF_MONTH) == day
    )
    {
        label = "I dag"
    }

    DateAndTimeButton(
        label,
        onClick = { datePicker.show() },
        icon = R.drawable.calendar,
        iconDescription = "Kalender icon",
        modifier = modifier
    )
}
