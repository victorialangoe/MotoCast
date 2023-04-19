package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.util.views.buttons.CloseButton


@Composable
fun DestinationButton(
    destinationIndex: Int,
    destinations: List<Destination>,
    editDestination: () -> Unit,
    removeDestination: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = editDestination,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier
                .size(56.dp)
                .weight(0.8f),
            shape = MaterialTheme.shapes.small,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()

            ) {

                // this is the left side
                Text(
                    modifier = Modifier.weight(0.1f),
                    text = when (destinationIndex) {
                        0 -> "Fra"
                        destinations.size - 1 -> "Til"
                        else -> "Via"
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )


                Text(
                    modifier = Modifier.weight(0.7f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                    ),
                    text = when (destinationIndex) {
                        0 -> destinations[destinationIndex].name ?: "Legg til startpunkt"
                        destinations.size - 1 -> destinations[destinationIndex].name
                            ?: "Legg til sluttpunkt"
                        else -> destinations[destinationIndex].name ?: "Legg til via punkt"
                    },
                )
                if (destinations.size > 2) {
                    CloseButton {
                        removeDestination()
                    }
                }
            }

        }
    }
}

