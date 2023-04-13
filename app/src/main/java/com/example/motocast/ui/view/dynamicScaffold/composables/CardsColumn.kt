package com.example.motocast.ui.view.dynamicScaffold.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.dynamicScaffold.cards.Card
import com.example.motocast.ui.viewmodel.route_planner.Destination
import java.util.*

@Composable
fun CardsColumn (destinations: List<Destination>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(destinations.size){index ->

            val location = destinations[index].name
            val timestampMillis = destinations[index].timestamp
            val date = Date(timestampMillis)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)

            Card(
                temperature = 12,
                location = location?: "",
                hours = hours,
                minutes = minutes,
                event = "blowingSnow",
                awarenessLevel = "2; yellow; Moderate"
            )
        }
/*
        item{
            Card(12, "Gjerstad", 10, 0, false,"blowingSnow" ,"2; yellow; Moderate")
        }

        item{
            Card(8, "Oslo", 12, 2,false,"","")
        }

        item{
            Card(-9, "Sandnessjøen", 9, 17,false,"","")
        }

        item{
            Card(12, "Gjerstad", 22, 0, true,"","")
        }

        item{
            Card(12, "Gjerstad", 10, 0,false,"","")
        }

        item{
            Card(12, "Gjerstad", 10, 0, true,"","")
        }*/
    }
}