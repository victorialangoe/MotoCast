package com.example.motocast.ui.view.dynamicScaffold.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamicScaffold.badges.DateBadge
import com.example.motocast.ui.view.dynamicScaffold.badges.TimeBadge

@Composable
fun DateAndTimeRow(month: Int, day: Int, hours: Int, minutes: Int){
    val fMonth = String.format("%02d", month+1)
    val fDay = String.format("%02d", day)
    val date = ("$fDay.$fMonth")

    val fHours = String.format("%02d", hours)
    val fMinutes = String.format("%02d", minutes)
    val time = ("$fHours:$fMinutes")

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        TimeBadge(time)

        Spacer(modifier = Modifier.width(50.dp))

        DateBadge(date)

    }
}
