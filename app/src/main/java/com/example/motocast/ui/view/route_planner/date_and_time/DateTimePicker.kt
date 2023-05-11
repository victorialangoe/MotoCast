package com.example.motocast.ui.view.route_planner.date_and_time

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun DateTimePicker(
    context: Context,
    startTime: Calendar,
    enabled: Boolean = true,
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
            modifier = Modifier.weight(1f, true),
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(16.dp))
        TimePicker(
            context = context,
            startTime = startTime,
            updateStartTime = updateStartTime,
            modifier = Modifier.weight(1f, true),
            enabled = enabled
        )
    }
}

