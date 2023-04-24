package com.example.motocast.ui.viewmodel.map

import android.location.Location
import android.util.Log
import android.view.Gravity
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.repository.MotoCastRepositoryInterface
import com.example.motocast.domain.use_cases.GetLocationUseCase
import com.example.motocast.theme.LightPrimary
import com.example.motocast.ui.viewmodel.route_planner.Destination
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
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min


@HiltViewModel
class MapViewModel @Inject constructor(
    private val motoCastRepositoryInterface: MotoCastRepositoryInterface,
    private val getLocationUseCase: GetLocationUseCase,
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

            val appContext = motoCastRepositoryInterface.getAppContext()
            val location = getLocationUseCase()

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
                                        .center(Point.fromLngLat(location.longitude, location.latitude))
                                        .zoom(15.0)
                                        .build(),
                                    mapAnimationOptions {
                                        duration(0)
                                    }
                                )
                            }
                            // Add mapbox logo to the map right top corner
                            this.logo.updateSettings {
                                position = Gravity.TOP or Gravity.END
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
        // set Loading to true
        updateUiState { it.copy(isLoading = true) }

        if (_uiState.value.mapView != null) {
            val mapboxMap = _uiState.value.mapView!!.getMapboxMap()
            Log.d("MapActivity", "Camera to user location")

            val location = getCurrentLocation()


            if (location != null) {
                Log.d("MapActivity", "Location: ${location.latitude}, ${location.longitude}")
                // Create a CameraOptions object with the user's location as the center
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
                    results // distanceBetween needs a float array to store the result
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

                /// stars an animation to smoothly go to the user, easeTo is way to laggy

                mapboxMap.flyTo(cameraOptions, mapAnimationOptions)


            } else {
                Log.d("MapActivity", "No location found")
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
    fun drawGeoJson(geoJsonString: String) {
        val mapView = _uiState.value.mapView
        mapView?.getMapboxMap()?.getStyle { style ->
            val sourceId = "geojson-source"
            val layerId = "geojson-layer"

            // Check if the source exists and update it, otherwise create and add the source
            if (style.getSource(sourceId) != null) {
                val geoJsonSource = style.getSourceAs<GeoJsonSource>(sourceId)
                geoJsonSource?.data(geoJsonString)
            } else {
                val geoJsonSource = geoJsonSource(sourceId) {
                    data(geoJsonString)
                }
                style.addSource(geoJsonSource)
            }

            // Check if the layer exists, otherwise create and add the layer
            if (style.getLayer(layerId) == null) {
                val lineLayer = lineLayer(layerId, sourceId) {
                    var hexColor = String.format("#%06X", 0xFFFFFF and LightPrimary.toArgb())
                    lineColor(hexColor)
                    lineWidth(5.0)
                }
                style.addLayerBelow(lineLayer, "road-intersection")
            }
        }
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
            .bearing(cameraPosition.bearing)
            .pitch(cameraPosition.pitch)
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

        if (!_uiState.value.trackUserOnMap && routeExists) {
            fitCameraToRouteAndWaypoints(destinations)
        } else {
            cameraToUserLocation()
        }
    }

    fun getCurrentLocation(): Location? {
        viewModelScope.launch {
            val location = getLocationUseCase()
            _uiState.value = _uiState.value.copy(lastLocation = location)
        }
        return _uiState.value.lastLocation
    }


}

