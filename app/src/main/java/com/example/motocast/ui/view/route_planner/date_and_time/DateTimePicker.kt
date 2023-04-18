package com.example.motocast.ui.view.route_planner.date_and_time

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun DateTimePicker(
    context: Context,
    startTime: Calendar,
    updateStartTime: (Calendar) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DatePicker(
            context = context,
            startTime = startTime,
            updateStartTime = updateStartTime,
            modifier = Modifier.weight(1f, true)
        )
        Spacer(modifier = Modifier.width(16.dp))
        TimePicker(
            context = context,
            startTime = startTime,
            updateStartTime = updateStartTime,
            modifier = Modifier.weight(1f, true)
        )
    }
}

