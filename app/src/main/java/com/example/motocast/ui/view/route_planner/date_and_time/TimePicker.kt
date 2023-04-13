package com.example.motocast.ui.view.route_planner.date_and_time

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerUiState
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
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
        label,
        onClick = { timePicker.show() },
        icon = R.drawable.clock,
        iconDescription = "Klokke icon",
        modifier = modifier
    )
}