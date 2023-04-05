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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.motocast.ui.view.getWeatherIcon

@Composable
fun CardWeather(temperature: Int, event: String, awarenessLevel: String) {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) } // Add a state variable to control the dialog
    val getMapIcons = getWeatherIcon(event, awarenessLevel)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$temperature °C", style = TextStyle(color = Color(0xff416788)))

        Spacer(modifier = Modifier.size(10.dp))

        ClickableImage(
            resourceName = getMapIcons,
            contentDescription = "My Image",
            showDialog = showDialog,
            onDismiss = { setShowDialog(false) },
            onClick = { setShowDialog(true) },
            event = event,
            awarenessLevel = awarenessLevel,
        )
    }

    // Pass the event and awarenessLevel to the dialog
    MyModalDialog(
        showDialog = showDialog,
        onDismiss = { setShowDialog(false) },
        event = event,
        awarenessLevel = awarenessLevel
    )
}



@Composable
fun ClickableImage(
    resourceName: String,
    contentDescription: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    event: String, // Add the event parameter
    awarenessLevel: String // Add the awarenessLevel parameter
) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    val painter = painterResource(resourceId)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.clickable(onClick = onClick)
    )

    // Some heavy prop drilling here lmao but we need the info!!
    MyModalDialog(
        showDialog = showDialog,
        onDismiss = onDismiss,
        event = event,
        awarenessLevel = awarenessLevel
    )
}





@Composable
fun MyModalDialog(
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


