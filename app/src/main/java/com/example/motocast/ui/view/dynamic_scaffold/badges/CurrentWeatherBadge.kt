package com.example.motocast.ui.view.dynamic_scaffold.badges

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.MainActivity
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel


@Composable
fun CurrentWeatherBadge(
    weatherViewModel: WeatherViewModel,
    context: Context
) {
    val nowCastUiState by weatherViewModel.uiState.collectAsState()
    // We don´t want to show anything if we don't have the data.
    if (nowCastUiState.temperature != null && nowCastUiState.symbolCode != null) {
        //
        Surface(
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // We have the loading so we don't have to re-render the whole row when the data is loaded.
                if (!nowCastUiState.isLoading) {
                    nowCastUiState.temperature?.let { temperature ->
                        Text(
                            text = "${temperature}°C",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    nowCastUiState.symbolCode?.let { symbolCode ->
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
            }
        }
    }
}

@Preview
@Composable
fun CurrentWeatherBadgePreview() {
    CurrentWeatherBadge(context = MainActivity(), weatherViewModel = WeatherViewModel())
}