package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun CardTimePlace(
    modifier: Modifier = Modifier,
    location: String,
    isInDestination: Boolean,
    time: Calendar?
) {

    val date = time?.let {
        val day = it.get(Calendar.DAY_OF_MONTH)
        val month = it.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val hour = it.get(Calendar.HOUR_OF_DAY)
        val minute = it.get(Calendar.MINUTE)

        "$day. $month ${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = location,
            style = if (!isInDestination) {
                MaterialTheme.typography.bodyMedium
            } else {
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            },
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.size(16.dp))

        if (date != null) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}