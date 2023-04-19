package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.util.badges.BasicBadge
import com.example.motocast.R

@Composable
fun DateTimeDurationRow(
    date: String,
    time: String,
) {
    Row(modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicBadge(leadingIcon = R.drawable.calendar, text = date)
        Spacer(modifier = Modifier.width(10.dp))
        BasicBadge(leadingIcon = R.drawable.clock, text = time)
    }
}