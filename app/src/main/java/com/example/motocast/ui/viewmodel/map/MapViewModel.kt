package com.example.motocast.ui.viewmodel.map

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.Gravity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.domain.use_cases.GetAppContextUseCase
import com.example.motocast.domain.use_cases.LocationUseCase
import com.example.motocast.theme.LightPrimary
import com.example.motocast.ui.view.map.ComposableWrapperView
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayerBelow
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min


@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAppContextUseCase: GetAppContextUseCase,
    private val locationUseCase: LocationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()


    private fun updateUiState(update: (MapUiState) -> MapUiState) {
        try {
            _uiState.value = update(_uiState.value)
        } catch (e: Exception) {
            Log.e("MapViewModel", "updateUiState: ${e.message}")
        }
    }

    /**
     * This function creates the map view and loads the map style.
     */
    fun loadMapView() {
        viewModelScope.launch(Dispatchers.Main) {

            val appContext = getAppContextUseCase()

            var location = locationUseCase.getCurrentLocation()
            var retries = 0
            while (location == null && retries < 10) {
                delay(1000) // 1 second
                location = locationUseCase.getCurrentLocation()
                retries++
                Log.d("CurrentWeatherViewModel", "Retry $retries: Location: $location")
            }

            if (_uiState.value.mapView == null) {
                updateUiState {
                    it.copy(
                        isInitialised = true,
                        mapView = MapView(appContext).apply {
                            this.getMapboxMap()
                                .loadStyleUri("mapbox://styles/mapbox/navigation-night-v1") {

                                    this.location.apply {
                                        enabled = true
                                        pulsingEnabled = true
                                    }
                                    //
                                    this.logo.apply {
                                        enabled = true
                                        updateSettings {
                                            position = Gravity.TOP or Gravity.END
                                        }
                                    }

                                }
                            //Set initial camera position
                            if (location != null) {
                                this.getMapboxMap().easeTo(
                                    CameraOptions.Builder()
                                        .center(
                                            Point.fromLngLat(
                                                location.longitude,
                                                location.latitude
                                            )
                                        )
                                        .zoom(15.0)
                                        .build(),
                                    mapAnimationOptions {
                                        duration(0)
                                    }
                                )
                            }


                            this.logo.updateSettings {
                                position = Gravity.TOP or Gravity.END
                            }

                            this.attribution.updateSettings {
                                enabled = false
                            }

                            this.scalebar.updateSettings {
                                enabled = false
                            }
                        })
                }
            }
        }
    }

    /**
     * This function centers the camera to the user's location.
     */
    private fun cameraToUserLocation() {
        updateUiState { it.copy(isLoading = true) }

        if (_uiState.value.mapView != null) {
            val mapboxMap = _uiState.value.mapView!!.getMapboxMap()
            Log.d("MapActivity", "Camera to user location")

            viewModelScope.launch(Dispatchers.Main) {
                val location = locationUseCase.getCurrentLocation()


                if (location != null) {
                    Log.d("MapActivity", "Location: ${location.latitude}, ${location.longitude}")
                    val currentCameraPosition = mapboxMap.cameraState
                    val targetCameraPosition = CameraOptions.Builder()
                        .center(Point.fromLngLat(location.longitude, location.latitude))
                        .zoom(15.0)
                        .build()

                    val results = FloatArray(1)

                    Location.distanceBetween(
                        currentCameraPosition.center.latitude(),
                        currentCameraPosition.center.longitude(),
                        targetCameraPosition.center?.latitude() ?: 0.0,
                        targetCameraPosition.center?.longitude() ?: 0.0,
                        results
                    )
                    val distance = results[0].toDouble()

                    val duration =
                        min(distance / 1000.0, 10.0).toLong() * 1000L // max duration of 10 seconds

                    val cameraOptions = CameraOptions.Builder()
                        .center(targetCameraPosition.center)
                        .zoom(targetCameraPosition.zoom)
                        .build()

                    val mapAnimationOptions = MapAnimationOptions.Builder()
                        .duration(duration)
                        .build()

                    mapboxMap.flyTo(cameraOptions, mapAnimationOptions)

                } else {
                    Log.d("MapActivity", "Location is null")
                }

            }
        }

        // set Loading to false
        updateUiState { it.copy(isLoading = false) }
    }


    /**
     * This function draws the route on the map.
     * Uses convert to geoJSON to convert the JSON response from the Mapbox Directions API to a GeoJSON string.
     * @param geoJsonString the JSON response from the Mapbox Directions API
     */
    fun drawGeoJson(geoJsonString: String, waypoints: List<RouteWithWaypoint>) {
        val mapView = _uiState.value.mapView

        if (mapView != null) {
            viewModelScope.launch(Dispatchers.Main) {
                val viewAnnotationManager = mapView.viewAnnotationManager
                val context = getAppContextUseCase()

                mapView.getMapboxMap().let { mapboxMap ->
                    mapboxMap.getStyle { style ->
                        val sourceId = "geojson-source"
                        val layerId = "geojson-layer"

                        if (style.getSource(sourceId) != null) {
                            val geoJsonSource = style.getSourceAs<GeoJsonSource>(sourceId)
                            geoJsonSource?.data(geoJsonString)
                        } else {
                            val geoJsonSource = geoJsonSource(sourceId) {
                                data(geoJsonString)
                            }
                            style.addSource(geoJsonSource)
                        }

                        if (style.getLayer(layerId) == null) {
                            val lineLayer = lineLayer(layerId, sourceId) {
                                val hexColor =
                                    String.format("#%06X", 0xFFFFFF and LightPrimary.toArgb())
                                lineColor(hexColor)
                                lineWidth(5.0)
                            }
                            style.addLayerBelow(lineLayer, "road-intersection")
                        }
                    }
                    if (_uiState.value.previousWaypoints != waypoints) {
                        viewAnnotationManager.removeAllViewAnnotations()
                        _uiState.value.previousWaypoints = waypoints
                        for (waypoint in waypoints) {
                            Log.d("MapView", "drawGeoJson: ${waypoint.weather?.temperature?.toInt() ?: 0}")
                            val point = Point.fromLngLat(
                                waypoint.longitude ?: 0.0,
                                waypoint.latitude ?: 0.0
                            )
                            Log.d("MapView", "drawGeoJson: ${point.latitude()}, ${point.longitude()}")
                            addViewAnnotation(
                                context = context,
                                point = point,
                                viewAnnotationManager = viewAnnotationManager,
                                waypoint = waypoint
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addViewAnnotation(
        point: Point,
        viewAnnotationManager: ViewAnnotationManager,
        waypoint: RouteWithWaypoint,
        context: Context
    ) {
        val view = ComposableWrapperView(
            context = context,
            temperature = (waypoint.weather?.temperature?.toInt() ?: 0),
            time = waypoint.timestamp,
            iconSymbol = waypoint.weather?.symbolCode ?: ""
        )

        // Measure the view to get the correct width and height
        Log.d("MapView", "addViewAnnotation: ${waypoint.weather?.temperature?.toInt() ?: 0}")
        viewAnnotationManager.addViewAnnotation(
            view,
            viewAnnotationOptions {
                geometry(point)
                allowOverlap(true) // Allow annotation to overlap with other annotations
                offsetY(100) // WE MAY USE THIS ON ANTHER VIEW
            }
        )


    }

    fun fitCameraToRouteAndWaypoints(destinations: List<Destination>) {
        val mapView = _uiState.value.mapView ?: return

        val waypoints = mutableListOf<Point>()
        destinations.forEach {
            if (it.latitude != null && it.longitude != null) {
                waypoints.add(Point.fromLngLat(it.longitude, it.latitude))
            }
        }

        val cameraPosition = mapView.getMapboxMap().cameraForCoordinates(
            waypoints,
            EdgeInsets(10.0, 100.0, 200.0, 100.0),
        )
        // Create a new camera position with a lower zoom level
        val updatedCameraPosition = CameraOptions.Builder()
            .center(cameraPosition.center)
            .zoom(cameraPosition.zoom?.minus(0.5)) // Decrease the zoom level by 1
            .build()

        mapView.getMapboxMap().easeTo(
            updatedCameraPosition,
            mapAnimationOptions {
                duration(1000L) // Set the duration of the animation in milliseconds
            }
        )
    }


    /**
     * This function is called when the user clicks on the "Track user on map" button.
     * It toggles the value of the trackUserOnMap variable.
     */
    fun trackUserOnMap(
        routeExists: Boolean,
        destinations: List<Destination>,
        track: Boolean? = null
    ) {
        _uiState.value = _uiState.value.copy(
            trackUserOnMap = track ?: !_uiState.value.trackUserOnMap
        )

        Log.d("MapActivity", "trackUserOnMap: ${_uiState.value.trackUserOnMap}")
        if (!_uiState.value.trackUserOnMap && routeExists) {
            fitCameraToRouteAndWaypoints(destinations)
        } else if (_uiState.value.trackUserOnMap) {
            cameraToUserLocation()
        }
    }


}

