package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.motocast.R
import java.util.*


@Composable
fun DatePicker(
    context: Context,
    modifier: Modifier = Modifier,
    updateStartTime: (Calendar) -> Unit,
    startTime: Calendar,
) {
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateStartTime(calendar)
            },
            startTime.get(Calendar.YEAR),
            startTime.get(Calendar.MONTH),
            startTime.get(Calendar.DAY_OF_MONTH),

        )
    }

    datePicker.datePicker.updateDate(
        startTime.get(Calendar.YEAR),
        startTime.get(Calendar.MONTH),
        startTime.get(Calendar.DAY_OF_MONTH),
    )
    datePicker.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE))
        updateStartTime(calendar)
    }

    datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis

    val label = "${startTime.get(Calendar.DAY_OF_MONTH)}. ${startTime.get(Calendar.MONTH) + 1}. ${startTime.get(Calendar.YEAR)}"

    // If the time is now, show "I dag" instead of the date
    val calendar = Calendar.getInstance()
    val isToday = calendar.get(Calendar.YEAR) == startTime.get(Calendar.YEAR) &&
            calendar.get(Calendar.MONTH) == startTime.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == startTime.get(Calendar.DAY_OF_MONTH)
    val labelToShow = if (isToday) "I dag" else label

    DateAndTimeButton(
        labelToShow,
        onClick = { datePicker.show() },
        icon = R.drawable.calendar,
        iconDescription = "Kalender icon",
        modifier = modifier
    )
}
