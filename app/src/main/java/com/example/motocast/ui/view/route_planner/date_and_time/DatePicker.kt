package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DateAndTimeButton
import java.util.*


@Composable
fun DatePicker(
    context: Context,
    modifier: Modifier = Modifier,
    updateStartTime: (Calendar) -> Unit,
    startTime: Calendar,
    enabled: Boolean = true,
) {
    val calendar = remember { mutableStateOf(startTime) }
    LaunchedEffect(Unit) {
        calendar.value = startTime
    }

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val updatedCalendar = Calendar.getInstance()
                updatedCalendar.set(Calendar.YEAR, year)
                updatedCalendar.set(Calendar.MONTH, monthOfYear)
                updatedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updatedCalendar.set(Calendar.HOUR_OF_DAY, calendar.value.get(Calendar.HOUR_OF_DAY))
                updatedCalendar.set(Calendar.MINUTE, calendar.value.get(Calendar.MINUTE))

                updateStartTime(updatedCalendar)
                calendar.value = updatedCalendar
            },
            calendar.value.get(Calendar.YEAR),
            calendar.value.get(Calendar.MONTH),
            calendar.value.get(Calendar.DAY_OF_MONTH),
        )
    }

    datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis

    val label =
        "${calendar.value.get(Calendar.DAY_OF_MONTH)}. ${calendar.value.get(Calendar.MONTH) + 1}. ${
            calendar.value.get(Calendar.YEAR)
        }"

    val currentCalendar = Calendar.getInstance()
    val isToday = currentCalendar.get(Calendar.YEAR) == calendar.value.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.MONTH) == calendar.value.get(Calendar.MONTH) &&
            currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.value.get(Calendar.DAY_OF_MONTH)
    val labelToShow = if (isToday) "I dag" else label

    DateAndTimeButton(
        modifier = modifier,
        label = labelToShow,
        onClick = { datePicker.show() },
        icon = R.drawable.calendar,
        enabled = enabled,
        iconDescription = "Kalender icon",
    )
}
