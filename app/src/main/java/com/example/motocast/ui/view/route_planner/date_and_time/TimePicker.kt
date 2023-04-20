package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.*
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DateAndTimeButton

@Composable
fun TimePicker(
    context: Context,
    modifier: Modifier = Modifier,
    startTime: Calendar,
    enabled: Boolean = true,
    updateStartTime: (Calendar) -> Unit,
) {
    val timePicker = remember {
        TimePickerDialog(context, { _, h, m ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)
            updateStartTime(calendar)
        }, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), true)

    }
    val label = "${startTime.get(Calendar.HOUR_OF_DAY)}:${startTime.get(Calendar.MINUTE)}"

    DateAndTimeButton(
        modifier = modifier,
        label = label,
        enabled = enabled,
        onClick = { timePicker.show() },
        icon = R.drawable.clock,
        iconDescription = "Klokke icon",
    )
}