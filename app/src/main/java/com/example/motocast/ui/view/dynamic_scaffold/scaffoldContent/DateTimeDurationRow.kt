package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DateAndTimeButton

@Composable
fun DateTimeDurationRow(
    date: String,
    time: String,
    duration: String,
    isLoading: Boolean,
) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row() {
            DateAndTimeButton(
                label = date,
                icon = R.drawable.calendar,
                iconDescription = "Calendar icon",
            )
            Spacer(modifier = Modifier.width(8.dp))
            DateAndTimeButton(
                label = time,
                icon = R.drawable.clock,
                iconDescription = "Clock icon",
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isLoading) {
            // Material3 ProgressIndicator
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = MaterialTheme.colorScheme.surface,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
}