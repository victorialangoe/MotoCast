package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.motocast.R
import okhttp3.internal.format
import java.util.*

@Composable
fun TimePicker(
    context: Context,
    modifier: Modifier = Modifier,
    updateStartTime: (Calendar) -> Unit,
    startTime: Calendar,
) {
    val updatedTime = Calendar.getInstance()
    updatedTime.set(Calendar.YEAR, startTime.get(Calendar.YEAR))
    updatedTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH))
    updatedTime.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH))

    val timePicker = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                updatedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                updatedTime.set(Calendar.MINUTE, minute)

                updateStartTime(updatedTime)
            },
            startTime.get(Calendar.HOUR_OF_DAY),
            startTime.get(Calendar.MINUTE),
            true
        )
    }

    val label = format(
        "%02d:%02d",
        startTime.get(Calendar.HOUR_OF_DAY),
        startTime.get(Calendar.MINUTE)
    )

    // If the time is now, show "Nå" instead of the time
    val calendar = Calendar.getInstance()
    val isNow = calendar.get(Calendar.HOUR_OF_DAY) == startTime.get(Calendar.HOUR_OF_DAY) &&
            calendar.get(Calendar.MINUTE) == startTime.get(Calendar.MINUTE)
    val labelToShow = if (isNow) "Nå" else label

    DateAndTimeButton(
        labelToShow,
        onClick = { timePicker.show() },
        icon = R.drawable.clock,
        iconDescription = "Klokke icon",
        modifier = modifier
    )
}
