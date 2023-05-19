package com.example.motocast.ui.view.route_planner.add_destinations

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.address.Address
import java.util.*

/**
 * A composable that displays a single address result
 *
 * @param address The address to display
 * @param onClick The callback to invoke when the address is clicked
 */

@Composable
fun AddressResult(
    address: Address,
    onClick: (address: Address) -> Unit,
    showInfo: Boolean = true,
    modifier: Modifier = Modifier,
    trailingIcon : @Composable () -> Unit = {}
)

{
    val distance = address.distanceFromUser?.let {
        if (it < 1000) {
            " - $it m"
        } else {
            " - ${(it / 1000)} km"
        }
    } ?: ""

    val municipality = address.municipality?.let {
        it.lowercase(Locale.ROOT).replaceFirstChar { char ->
            if (char.isLowerCase()) char.uppercase(Locale.ROOT) else char.toString()
        }
    } ?: ""


    Button(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth(),
        onClick = { onClick(address) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = Modifier.semantics(mergeDescendants = true) { },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = address.addressText,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
            if (showInfo) {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "$municipality$distance",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(0.8f, fill = true)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }
    }
}