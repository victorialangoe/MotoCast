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
    routePlannerViewModel: RoutePlannerViewModel,
    context: Context = LocalContext.current
) {

    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()


// Fetching current hour, and minute
    val hour = routePlannerUiState.startTime.timePickerUiState.hour
    val minute = routePlannerUiState.startTime.timePickerUiState.minute

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            routePlannerViewModel.updateTimeUiState(
                TimePickerUiState(
                    selectedHour,
                    selectedMinute
                )
            )
        }, hour, minute, true
    )

    Button(
        onClick = {
        timePicker.show()
    },
        shape = RoundedCornerShape(size = 8.dp),
        colors = ButtonDefaults.buttonColors(Color(0xfff7f7f7)),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 55.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.clock),
                contentDescription = "Clock icon",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                text = "${routePlannerUiState.startTime.timePickerUiState.hour} : ${routePlannerUiState.startTime.timePickerUiState.minute}",
                color = Color.Black)
        }
    }
}
