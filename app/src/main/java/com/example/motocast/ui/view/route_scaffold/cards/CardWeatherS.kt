package com.example.motocast.ui.view.dynamicScaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.motocast.R

@Composable
fun CardWeatherS(temperature: Int, fare: Boolean) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = "$temperature °C", style = TextStyle(color = Color(0xff416788)))

        Spacer(modifier = Modifier.size(10.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_wb_sunny_24),
            contentDescription = "Sunny icon",
        )

        

        if (fare){
            //Fare-condition
            Spacer(modifier = Modifier.size(6.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.danger_windy),
                contentDescription = "Danger windy icon",
            )
        }
    }
}