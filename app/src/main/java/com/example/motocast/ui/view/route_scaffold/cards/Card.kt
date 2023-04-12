package com.example.motocast.ui.view.route_scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R
import com.mapbox.maps.extension.style.expressions.dsl.generated.image

@Composable
fun Card (temperature: Int, location: String, hours: Int, minutes: Int, fare: Boolean = false, event: String, awarenessLevel: String) {

    Box(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(color = Color(0xfff7f7f7))
        .fillMaxWidth())
    {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)) {

            CardTimePlace(location = location, hours = hours, minutes = minutes)

            Spacer(modifier = Modifier.weight(1f))

            CardWeather(temperature, fare)
        }
    }
}