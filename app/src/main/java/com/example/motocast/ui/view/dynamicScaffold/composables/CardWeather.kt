package com.example.motocast.ui.view.dynamicScaffold.composables

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.motocast.R

@Composable
fun CardWeather(temperature: Double? = null,
                fare: Boolean = true,
                symbolCode: String? = null,
                context: Context)
{

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val image = ImageVector.vectorResource(id = R.drawable.danger_windy) // this will be from api call

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = "$temperature °C", style = TextStyle(color = Color(0xff416788)))

        Spacer(modifier = Modifier.size(10.dp))

        //TODO: anbefalt å ikke bruke getIdentifier. Kanskje lage et hashMap med alle verdiene
        val resourceId = context.resources.getIdentifier(symbolCode, "drawable", context.packageName)
        Image(
            painter = painterResource(id = resourceId),
            //imageVector = ImageVector.vectorResource(id = resourceId),
            contentDescription = symbolCode,
            modifier = Modifier.size(24.dp)
        )

        Log.d("CardWeather", "fare: $fare")

        if (fare){
            //Fare-condition
            Spacer(modifier = Modifier.size(6.dp))
            ClickableImage(showDialog = showDialog,
                onDismiss = { setShowDialog(false) },
                onClick = { setShowDialog(true) },
                event = "",
                awarenessLevel = "",
                image = image
            )
        }
    }

    CardInfoDialog(
        showDialog = showDialog,
        onDismiss = { setShowDialog(false) },
        event = "",
        awarenessLevel = ""
    )
}