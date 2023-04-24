package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Displays the current weather in a card.
 * @param modifier Modifier to be applied to the card.
 * @param temperature The temperature to be displayed.
 * @param symbolCode The symbol code of the weather icon.
 * @param context The context of the application. (Hack to get the symbol code from the string resource)
 */
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
}
