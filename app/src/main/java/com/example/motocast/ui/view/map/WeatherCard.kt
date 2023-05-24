package com.example.motocast.ui.view.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.motocast.domain.utils.Utils.createReverseTriangleShape
import com.example.motocast.theme.Blue700
import com.example.motocast.theme.Red700
import java.text.SimpleDateFormat
import java.util.*

// As already mentioned in the DisplayWeather.kt file, we suppress the warning here as well
// for the same reason.
@SuppressLint("DiscouragedApi")
@Composable
fun WeatherCard(
    temperature: Int,
    time: Calendar?,
    iconSymbol: String,
    context: Context,
) {
    Column {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "$temperature°",
                        color = if (temperature < 0) {
                            Blue700
                        } else {
                            Red700
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (time != null) {
                        Text(
                            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (iconSymbol.isNotEmpty()) {
                    Image(
                        painter = painterResource(
                            id = context.resources.getIdentifier(
                                iconSymbol,
                                "drawable",

                                context.packageName
                            )
                        ),
                        contentDescription = iconSymbol,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.White, createReverseTriangleShape())
                .size(20.dp, 10.dp)
        )
    }
}

