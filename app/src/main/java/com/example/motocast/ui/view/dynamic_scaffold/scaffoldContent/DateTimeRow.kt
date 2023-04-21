package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.route_planner.buttons.DateAndTimeButton

@Composable
fun DateTimeRow(
    date: String,
    time: String,
) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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
    }
}