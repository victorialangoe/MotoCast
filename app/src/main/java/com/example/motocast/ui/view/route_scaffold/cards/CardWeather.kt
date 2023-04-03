package com.example.motocast.ui.view.route_scaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.motocast.R

@Composable
fun CardWeather(temperature: Int, fare: Boolean) {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) } // Add a state variable to control the dialog

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$temperature °C", style = TextStyle(color = Color(0xff416788)))

        Spacer(modifier = Modifier.size(10.dp))

        ClickableImage(
            painter = painterResource(R.drawable.baseline_wb_sunny_24),
            contentDescription = "My Image",
            showDialog = showDialog,
            onDismiss = { setShowDialog(false) },
            onClick = { setShowDialog(true) }
        )

        if (fare) {
            // Fare-condition
            Spacer(modifier = Modifier.size(6.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.danger_windy),
                contentDescription = "Danger windy icon"
            )
        }
    }

}


@Composable
fun ClickableImage(
    painter: Painter,
    contentDescription: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.clickable(onClick = onClick)
    )

    MyModalDialog(
        showDialog = showDialog,
        onDismiss = onDismiss
    )
}


@Composable
fun MyModalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
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
                    Text("Modal Title")
                    Text("Modal Message")
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

