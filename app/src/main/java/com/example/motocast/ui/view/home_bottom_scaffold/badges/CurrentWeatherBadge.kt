package com.example.motocast.ui.view.home_bottom_scaffold.badges

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.MainActivity

@Composable
fun CurrentWeatherBadge(
    temperature: Double,
    symbolCode: String,
    context: Context
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(32.dp))
            .background(color = Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "$temperature°C", color = Color.Black)
        Log.d("CurrentWeatherBadge", "symbolCode: $symbolCode")
        if (symbolCode != "") {
            Image(
                painter = painterResource(
                    id = context.resources.getIdentifier(
                        "$symbolCode",
                        "drawable",
                        context.packageName
                    )
                ),
                contentDescription = "Weather icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Preview

@Composable
fun CurrentWeatherBadgePreview() {
    CurrentWeatherBadge(temperature = 20.00, symbolCode = "01d", context = MainActivity().applicationContext)
}