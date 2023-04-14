package com.example.motocast.ui.view.dynamicScaffold.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.nowcast.NowCastUiState
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.route_planner.Destination
import java.util.*

@Composable
fun CardsColumn (destinations: List<Destination>, nowCastViewModel: NowCastViewModel) {

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(destinations.size){index ->

            val location = destinations[index].name
            val timestampMillis = destinations[index].timestamp
            val date = Date(timestampMillis)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)

            var temperatureNow: Double? by remember { mutableStateOf(null) }

            nowCastViewModel.getTemperature(
                latitude = destinations[index].latitude,
                longitude = destinations[index].longitude,
                onSuccess = {temperature -> temperatureNow = temperature}
            )

            Log.d("CardsColumn", "$location temperature: $temperatureNow")

            if (temperatureNow != null){
                Card(
                    temperature = temperatureNow,
                    location = location?: "",
                    hours = hours,
                    minutes = minutes,
                    event = "blowingSnow",
                    awarenessLevel = "2; yellow; Moderate"
                )
            }

        }
    }
}