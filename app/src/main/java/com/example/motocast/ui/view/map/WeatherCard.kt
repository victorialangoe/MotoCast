package com.example.motocast.ui.view.map

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
import com.example.motocast.theme.Blue700
import com.example.motocast.theme.Red700
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun WeatherCard(
    temperature: Int,
    location: String,
    time: Calendar?,
    fare: Boolean = false,
    iconSymbol: String,
    context: Context,
) {
    Column() {


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
                            text = SimpleDateFormat("HH:mm").format(time.time),
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
                .background(Color.White, ReverseTriangleShape)
                .size(20.dp, 10.dp)
        )

    }
}

private val ReverseTriangleShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width / 2f, size.height)
    lineTo(size.width, 0f)
}