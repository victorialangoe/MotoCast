package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R

@Composable
fun DisplayWeather(
    temperature: Int,
    fare: Boolean,
    symbolCode: String?,
    context: android.content.Context
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
    ) {

        Text(
            color = MaterialTheme.colorScheme.onSurface,
            text = "$temperature °C",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.size(8.dp))

        if (symbolCode != null && symbolCode.isNotEmpty()) {
            symbolCode.let { symbolCode ->
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
                    modifier = Modifier.size(30.dp),
                )
            }
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
