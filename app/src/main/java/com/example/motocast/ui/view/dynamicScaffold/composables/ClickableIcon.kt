package com.example.motocast.ui.view.dynamicScaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun ClickableImage(
    //resourceName: String,
    //contentDescription: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    event: String, // Add the event parameter
    awarenessLevel: String, // Add the awarenessLevel parameter
    image: ImageVector
) {
    //val context = LocalContext.current
    //val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    //val painter = painterResource(resourceId)

    Image(
        imageVector = image,
        contentDescription = "Sunny icon",
        modifier = Modifier.clickable(onClick = onClick)
    )

    // Some heavy prop drilling here lmao but we need the info!!
    CardInfoDialog(
        showDialog = showDialog,
        onDismiss = onDismiss,
        event = event,
        awarenessLevel = awarenessLevel
    )
}