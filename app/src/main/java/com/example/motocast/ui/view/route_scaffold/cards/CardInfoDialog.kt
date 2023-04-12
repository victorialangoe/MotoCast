package com.example.motocast.ui.view.route_scaffold.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CardInfoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    event: String,
    awarenessLevel: String
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth() // aka takes up as much space it needs to display info
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Event: $event")
                    Text("Awareness Level: $awarenessLevel")
                    // Here we will props need description....
                    Text("Here is some info title (event)")
                    Text("Here is some description that we get from metAlerts (val information = properties.description)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }
        }
    }
}