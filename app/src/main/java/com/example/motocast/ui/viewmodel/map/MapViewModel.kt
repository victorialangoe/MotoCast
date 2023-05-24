package com.example.motocast.ui.viewmodel.map

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.Gravity
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun loadMapView() {
        viewModelScope.launch(Dispatchers.Main) {

            val appContext = getAppContextUseCase()

            var location = locationUseCase.getCurrentLocation()
            var retries = 0
            while (location == null && retries < 10) {
                /**
                 * Why we do this is described in fetchNowCastDataEvery5min in CurrentWeatherViewModel
                 */
                delay(1000)
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

    private fun cameraToUserLocation() {
        updateUiState { it.copy(isLoading = true) }

        if (_uiState.value.mapView != null) {
            val mapboxMap = _uiState.value.mapView!!.getMapboxMap()

            viewModelScope.launch(Dispatchers.Main) {
                val location = locationUseCase.getCurrentLocation()


                if (location != null) {
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

        updateUiState { it.copy(isLoading = false) }
    }


    /**
     * This function draws the route on the map.
     * Uses convert to geoJSON to convert the JSON response from the Mapbox Directions API to a GeoJSON string.
     * It also draws the view annotations for the waypoints.
     *
     * @param geoJsonString the JSON response from the Mapbox Directions API
     * @param waypoints the waypoints to draw on the map
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

                            val point = Point.fromLngLat(
                                waypoint.longitude ?: 0.0,
                                waypoint.latitude ?: 0.0
                            )

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

    /**
     * This function adds a view annotation to the map, which are the cards with the weather information for the
     * waypoints on the map.
     *
     * @param point the point to add the view annotation to
     * @param viewAnnotationManager the view annotation manager of the map
     * @param waypoint the waypoint to add the view annotation for
     * @param context the context of the application
     */
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

        viewAnnotationManager.addViewAnnotation(
            view,
            viewAnnotationOptions {
                geometry(point)
                allowOverlap(true)
                offsetY(100)
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

        val updatedCameraPosition = CameraOptions.Builder()
            .center(cameraPosition.center)
            .zoom(cameraPosition.zoom?.minus(0.5))
            .build()

        mapView.getMapboxMap().easeTo(
            updatedCameraPosition,
            mapAnimationOptions {
                duration(1000L)
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

        if (!_uiState.value.trackUserOnMap && routeExists) {
            fitCameraToRouteAndWaypoints(destinations)
        } else if (_uiState.value.trackUserOnMap) {
            cameraToUserLocation()
        }
    }

}

