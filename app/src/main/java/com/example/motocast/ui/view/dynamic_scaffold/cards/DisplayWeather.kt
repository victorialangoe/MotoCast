package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.motocast.theme.Blue700
import com.example.motocast.theme.Red700

// We need to suppress this warning because this was the only way we managed to get the
// symbolCode to be displayed in the UI. This technical debt should be fixed in the future.
@SuppressLint("DiscouragedApi")
@Composable
fun DisplayWeather(
    modifier: Modifier = Modifier,
    temperature: Int,
    symbolCode: String?,
    context: android.content.Context
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 8.dp, horizontal = 12.dp)
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

        Spacer(modifier = Modifier.size(8.dp))

        if (symbolCode != null && symbolCode.isNotEmpty()) {
            symbolCode.let { symbolCode ->
                val resourceId = context.resources.getIdentifier(symbolCode, "drawable", context.packageName)
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = symbolCode,
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}
