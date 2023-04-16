package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun CardWeather(
    temperature: Int,
    fare: Boolean,
    symbolCode: String?,
    context: android.content.Context
) {
    Log.d("CardWeather", "symbolCode: $symbolCode")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "$temperature °C", style = TextStyle(color = Color(0xff416788)))

        Spacer(modifier = Modifier.size(10.dp))

        symbolCode?.let { symbolCode ->
            Image(
                painter = painterResource(
                    // TODO: This is a hack to get the symbol code from the string resource. We might want to change this.
                    id = context.resources.getIdentifier(
                        symbolCode,
                        "drawable",
                        context.packageName
                    )
                ),
                contentDescription = symbolCode,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (fare) {
        //Fare-condition
        Spacer(modifier = Modifier.size(6.dp))
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.danger_windy),
            contentDescription = "Danger windy icon",
        )
    }
}
