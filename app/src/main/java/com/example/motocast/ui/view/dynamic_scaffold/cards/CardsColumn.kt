package com.example.motocast.ui.view.dynamic_scaffold.cards

import android.content.Context
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.maps.extension.style.expressions.dsl.generated.indexOf
import com.mapbox.maps.extension.style.sources.generated.vectorSource

@Composable
fun CardsColumn(waypoints: List<RouteWithWaypoint>, context: Context) {
    Modifier.padding()
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(450.dp)) {
        items(waypoints.size) { index ->
            val weather = waypoints[index].weatherUiState
            val time = waypoints[index].timestamp
            if (weather != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    when(index){
                        0 -> Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.start_point),
                            contentDescription = "icon for start destination"
                        )
                        waypoints.size-1 -> Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.destination_point),
                            contentDescription = "icon for destination"
                        )
                        else -> Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.stop_point),
                            contentDescription = "midlertidig"
                        ) //TODO: finne ut måte å skille waypoints fra destination på
                    }

                    Spacer(Modifier.width(10.dp))

                    Card(
                        temperature = weather.temperature?.toInt() ?: 0,
                        location = waypoints[index].name ?: "",
                        time = time,
                        iconSymbol = weather.symbolCode ?: "",
                        context = context
                    )
                }
            }
        }
    }
}