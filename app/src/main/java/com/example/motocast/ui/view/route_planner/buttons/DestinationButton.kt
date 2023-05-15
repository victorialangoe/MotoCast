package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.ui.view.utils.buttons.CloseButton


@Composable
fun DestinationButton(
    destinationIndex: Int,
    destinations: List<Destination>,
    editDestination: () -> Unit,
    removeDestination: () -> Unit,
    enabledRemove: Boolean,
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
                    .semantics(mergeDescendants = true) {}

            ) {

                // this is the left side
                Text(
                    modifier = Modifier.weight(0.1f),
                    text = when (destinationIndex) {
                        0 -> stringResource(R.string.from)
                        destinations.size - 1 -> stringResource(R.string.to)
                        else -> stringResource(R.string.via)
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
                        0 -> destinations[destinationIndex].name ?: stringResource(R.string.add_start_point)
                        destinations.size - 1 -> destinations[destinationIndex].name
                            ?: stringResource(R.string.add_end_point)
                        else -> destinations[destinationIndex].name ?: stringResource(R.string.add_via_point)
                    },
                )
                if (destinations.size > 2) {
                    CloseButton(
                        onClick = removeDestination,
                        enabled = enabledRemove,
                    )
                }
            }

        }
    }
}

