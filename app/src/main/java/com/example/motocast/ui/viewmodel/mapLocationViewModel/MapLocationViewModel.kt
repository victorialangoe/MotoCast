package com.example.motocast.ui.viewmodel.mapLocationViewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.motocast.ui.viewmodel.route_planner.Destination
import com.google.android.gms.location.*
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*


/**
 * ViewModel for MapLocationViewModel. Map and Location related functions are here,
 * they are merged together because they are closely related and need to be used together.
 */
class MapLocationViewModel(
    private val activity: Activity,
) : LocationCallback() {

    private val locationRequest: LocationRequest by lazy { createRequest() }
    private val _uiState = MutableStateFlow(MapLocationUiState())
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity.applicationContext)
    }
    private var timeInterval = 100L // 100 milliseconds
    private val minimalDistance = 1f // 0 meters
    val uiState = _uiState.asStateFlow()


    /**
     * This function creates the map view and loads the map style.
     */
    fun loadMapView(context: Context) {
        if (_uiState.value.mapView == null) {
            try {
                _uiState.value = _uiState.value.copy(
                    mapView = MapView(context).apply {
                        val mapView = this
                        mapView.getMapboxMap()
                            .loadStyleUri("mapbox://styles/mapbox/navigation-day-v1") {
                                mapView.location.apply {
                                    enabled = true
                                    pulsingEnabled = true
                                }


                                _uiState.value = _uiState.value.copy(isLoading = false)
                            }
                        mapView.logo.updateSettings {
                            position = Gravity.TOP or Gravity.RIGHT

                        }

                    }
                )
            }
            catch (e: Exception) {
                Log.d("MapActivity", "Error: ${e.message}")
            }
        }
    }

    /**
     * This function centers the camera to the user's location.
     */
    private fun cameraToUserLocation() {
        // set Loading to true
        _uiState.value = _uiState.value.copy(isLoading = true)
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
        _uiState.value = _uiState.value.copy(isLoading = false)
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
                    lineColor("#00b4d8")
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
     * This function checks if the user has granted the location permission.
     */
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * This function asks the user for the location permission.
     */
    private fun getPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            6969
        )
    }

    /**
     * Creates a LocationRequest object with the desired parameters.
     * We can create another to update the parameters in the future.
     */
    private fun createRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    /**
     * This function starts the location tracking. It checks if the user has granted the location
     * permission and if so, it starts the location tracking. If the user has not granted the
     * permission, it requests the permission.
     */
    fun startLocationTracking() {
        if (checkPermission()) {
            // get current location
            locationClient.requestLocationUpdates(locationRequest, this, Looper.getMainLooper())
            val location = getCurrentLocation()
            if (location != null) {

                cameraToUserLocation()
            }
        } else {
            getPermission()
        }
    }

    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    /**
     * This function is called when the user clicks on the "Track user on map" button.
     * It toggles the value of the trackUserOnMap variable.
     */
    fun trackUserOnMap(routeExists: Boolean, destinations: List<Destination>) {
        _uiState.value = _uiState.value.copy(trackUserOnMap = !_uiState.value.trackUserOnMap)

        if (!_uiState.value.trackUserOnMap && routeExists) {
            fitCameraToRouteAndWaypoints(destinations)
        } else {
            cameraToUserLocation()
        }
    }

    /**
     * This function is called when the user clicks on the "Get current location" button.
     * It checks if the user has granted the location permission and if so, it gets the last
     * known location. If the user has not granted the permission, it requests the permission.
     */
    fun getCurrentLocation(): Location? {
        if (checkPermission()) {
            return _uiState.value.lastLocation
        } else {
            getPermission()
        }
        return null
    }

    /**
     * Returns the distance between the user's location and the given location.
     * @param lat1 latitude of the given location
     * @param lon1 longitude of the given location
     * @param location user's location
     * @return returns distance in meters
     */
    fun getAirDistanceFromLocation(location: Location): Int? {
        if (_uiState.value.lastLocation != null) {
            try {
                val lat1 = location.latitude
                val lon1 = location.longitude

                val lat2 = _uiState.value.lastLocation!!.latitude
                val lon2 = _uiState.value.lastLocation!!.longitude

                val earthRadius = 6371 // kilometers
                val dLat = Math.toRadians(lat2 - lat1)
                val dLon = Math.toRadians(lon2 - lon1)
                val lat1Rad = Math.toRadians(lat1)
                val lat2Rad = Math.toRadians(lat2)

                val a = sin(dLat / 2) * sin(dLat / 2) +
                        sin(dLon / 2) * sin(dLon / 2) * cos(lat1Rad) * cos(lat2Rad)
                val c = 2 * atan2(sqrt(a), sqrt(1 - a))
                val distance = earthRadius * c * 1000 // meters

                return distance.toInt()
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error calculating distance: ${e.message}")
                return null
            }
        }
        return null
    }

    /**
     * This function is called when the location is updated. It checks if the new location is
     * different enough from the last location to update the map.
     */
    override fun onLocationResult(result: LocationResult) {
        super.onLocationResult(result)
        if (_uiState.value.isLoading) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)


        val location = result.lastLocation
        val lastLocation = _uiState.value.lastLocation



        if (lastLocation != null && location != null) {
            val distance = location.distanceTo(lastLocation)
            if (distance > minimalDistance) {
                _uiState.value = _uiState.value.copy(lastLocation = location)
                // If the user has selected the "Track user on map" option, the map will be moved
                if (_uiState.value.trackUserOnMap) {
                    cameraToUserLocation()
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(lastLocation = location)
        }

        _uiState.value = _uiState.value.copy(isLoading = false)
    }


    /**
     * This function is called when the location availability changes. If the location is not
     * available, it requests the location updates again.
     */
    override fun onLocationAvailability(availability: LocationAvailability) {
        if (!checkPermission()) {
            getPermission()
        }
    }
}

