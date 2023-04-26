package com.example.motocast.ui.view.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.motocast.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.GenericShape
import com.example.motocast.theme.Blue700
import com.example.motocast.theme.Red700


@Composable
fun MapView(
    geoJsonData: String? = null,
    mapView: MapView? = null,
    drawGeoJson: (String) -> Unit,
    onInit: () -> Unit,
    bottomOffset: Int = 0,
    waypoints: List<RouteWithWaypoint>,
    context: Context
) {

    onInit()


    if (geoJsonData != null) {
        drawGeoJson(geoJsonData)
    }

    if (mapView != null) {
        MapViewContent(mapView, bottomOffset)
        val viewAnnotationManager = mapView.viewAnnotationManager
        val previousWaypoints = remember { mutableStateOf(emptyList<RouteWithWaypoint>()) }

        if (previousWaypoints.value != waypoints) {
            viewAnnotationManager.removeAllViewAnnotations()
            previousWaypoints.value = waypoints
        }

        for (waypoint in waypoints) {
            Log.d("MapViewAnnotation", "waypoint: $waypoint")
            val point = Point.fromLngLat(
                waypoint.longitude ?: 0.0,
                waypoint.latitude ?: 0.0
            )
            addViewAnnotation(context = context, point = point, viewAnnotationManager = viewAnnotationManager, waypoint = waypoint)
        }
    } else {
        Text(
            text = stringResource(R.string.loading_map),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
fun addViewAnnotation(
    point: Point,
    viewAnnotationManager: ViewAnnotationManager,
    waypoint: RouteWithWaypoint,
    context: Context
) {
    val view = ComposableWrapperView(
        context = context,
        temperature = (waypoint.weather?.temperature?.toInt() ?: 0),
        location = waypoint.name ?: "Ukjent",
        time = waypoint.timestamp,
        iconSymbol = waypoint.weather?.symbolCode ?: ""
    )

    // Measure the view to get the correct width and height

    viewAnnotationManager.addViewAnnotation(
        view,
        viewAnnotationOptions {
            geometry(point)
            allowOverlap(true) // Allow annotation to overlap with other annotations
            offsetY(100)
        }
    )
}

class ComposableWrapperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val temperature: Int,
    private val location: String,
    private val time: Calendar?,
    private val iconSymbol: String
) : FrameLayout(context, attrs, defStyleAttr) {

    init {


        val composeView = ComposeView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setContent {
                WeatherCard(
                    temperature = temperature,
                    location = location,
                    time = time,
                    iconSymbol = iconSymbol,
                    fare = true,
                    context = context
                )
            }
        }
        addView(composeView)

        // Set the layoutParams for the ComposableWrapperView itself
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

}
private val ReverseTriangleShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width / 2f, size.height)
    lineTo(size.width, 0f)
}

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
                .background(MaterialTheme.colorScheme.surface)
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
                .background(MaterialTheme.colorScheme.surface, ReverseTriangleShape)
                .size(20.dp, 10.dp)
        )

    }
}









