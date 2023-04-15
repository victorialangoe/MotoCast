package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.util.buttons.BasicButton


@Composable
fun DestinationButton(
    destinationIndex: Int,
    destinations: List<Destination>,
    editDestination: () -> Unit,
    removeDestination: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // this is the left side
        Text(
            text = when (destinationIndex) {
                0 -> "Fra"
                destinations.size - 1 -> "Til"
                else -> "Via"
            },
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier.weight(0.12f, fill = true)
        )
        // this is the center
        BasicButton(
            centerContent = false,
            fontSize = 18,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 55.dp)
                .weight(0.76f, fill = true),
            text = when (destinationIndex) {
                0 -> destinations[destinationIndex].name ?: "Legg til startpunkt"
                destinations.size - 1 -> destinations[destinationIndex].name
                    ?: "Legg til sluttpunkt"
                else -> destinations[destinationIndex].name ?: "Legg til via punkt"
            },
            outlined = true,
            onClick = { editDestination() },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = ImageVector.vectorResource(
                        id = when (destinationIndex) {
                            0 -> R.drawable.line_end_arrow_rounded
                            destinations.size - 1 -> R.drawable.mdi_goal
                            else -> R.drawable.format_line_spacing_rounded
                        }
                    ),
                    contentDescription = when (destinationIndex) {
                        0 -> "Fra icon for input felt"
                        destinations.size - 1 -> "Til icon for input felt"
                        else -> "Via icon for input felt"
                    }
                )
            }
        )
        // this is the right side
        if (destinations.size > 2) {
            TextButton(onClick = { removeDestination() }) {
                Text(text = "Fjern", color = Color.Black)
            }
        }
    }
}
