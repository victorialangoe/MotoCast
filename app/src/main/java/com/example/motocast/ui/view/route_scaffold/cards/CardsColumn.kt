package com.example.motocast.ui.view.route_scaffold.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.getWeatherIcon

@Composable
fun CardsColumn (event: String, awarenessLevel: String) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){

        item{
            Card(12, "Gjerstad", 10, 0, event, awarenessLevel)
        }

        item{
            Card(8, "Oslo", 12, 2, event, awarenessLevel)
        }

        item{
            Card(-9, "Sandnessjøen", 9, 17,event, awarenessLevel)
        }

        item{
            Card(12, "Gjerstad", 22, 0,event,awarenessLevel)
        }

        item{
            Card(12, "Gjerstad", 10, 0,event,awarenessLevel)
        }

        item{
            Card(12, "Gjerstad", 10, 0,event,awarenessLevel)
        }
    }
}