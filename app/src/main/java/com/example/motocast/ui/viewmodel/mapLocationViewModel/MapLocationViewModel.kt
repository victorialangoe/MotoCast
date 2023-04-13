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
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.directions.DirectionsHelper
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.google.android.gms.location.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayerBelow
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.min
import kotlin.math.*


/**
 * ViewModel for MapLocationViewModel. Map and Location related functions are here,
 * they are merged together because they are closely related and need to be used together.
 */
class  MapLocationViewModel(
    private val activity: Activity,
    private var timeInterval: Long,
    private var minimalDistance: Float,
    private val bigDistanceChange: Float = 100_000f, // 100 km
    private val nowCastViewModel: NowCastViewModel,

    ) : LocationCallback() {

    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity.applicationContext)
    }
    private val request: LocationRequest by lazy {
        createRequest()
    }
    private val _uiState = MutableStateFlow(MapUiState())

    val uiState = _uiState.asStateFlow()

    /**
     * This function creates the map view and loads the map style.
     */
    fun loadMapView(context: Context) {
        if (_uiState.value.mapView == null) {
            _uiState.value = _uiState.value.copy(
                mapView = MapView(context).apply {
                    val mapView = this
                    mapView.getMapboxMap()
                        .loadStyleUri("mapbox://styles/mapbox/navigation-day-v1") {
                            mapView.location.apply {
                                enabled = true
                                pulsingEnabled = true
                            }
                            val origin = Point.fromLngLat(10.717983,59.942730)
                            val destination = Point.fromLngLat(10.447102, 61.114927)
                            val accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
                            getRoute(context, origin, destination, accessToken)

                            _uiState.value = _uiState.value.copy(isLoading = false)
                        }
                    mapView.logo.updateSettings {
                        position = Gravity.TOP or Gravity.RIGHT

                    }

                }
            )
        }
    }

    /**
     * This function centers the camera to the user's location.
     */
    fun cameraToUserLocation() {
        if (_uiState.value.mapView != null) {
            val mapboxMap = _uiState.value.mapView!!.getMapboxMap()
            Log.d("MapActivity", "Camera to user location")

            getCurrentLocation(
                // Handle the success, e.g., get the location and update the UI
                { location ->
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


                },
                // Handle the error, e.g., show a message to the user or log the error
                { exception ->
                    // Handle the error, e.g., show a message to the user or log the error
                    Log.d("MapViewModel", "Error: ${exception.localizedMessage}")
                }
            )
        }
    }

    private fun getRoute(context: Context, origin: Point, destination: Point, accessToken: String) {
        val coordinates = "${origin.longitude()},${origin.latitude()};" +
                "${destination.longitude()},${destination.latitude()}"




        val directionsHelper = DirectionsHelper()
        val service = directionsHelper.createDirectionsAPI()
        val call = service.getDirections(
            coordinates = coordinates,
            accessToken = accessToken,
            alternatives = false,
            geometries = "geojson",
            language = "en",
            overview = "simplified",
            steps = true
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val jsonResponse = responseBody.string()
                        drawGeoJson(jsonResponse)
                    }
                } else {
                    Log.e("MapActivity", "Error getting route: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MapActivity", "Error getting route: ${t.message}")
            }
        })
    }

    /**
     * This function draws the route on the map.
     * Uses convert to geoJSON to convert the JSON response from the Mapbox Directions API to a GeoJSON string.
     * @param jsonString the JSON response from the Mapbox Directions API
     */
    private fun drawGeoJson(jsonString: String) {
        val mapView = _uiState.value.mapView
        mapView?.getMapboxMap()?.getStyle { style ->

            val geoJSONString = convertToGeoJSON(jsonString)

            val geoJsonSource = geoJsonSource("geojson-source") {
                data(geoJSONString)
            }
            style.addSource(geoJsonSource)
            style.addLayerBelow(
                lineLayer("geojson-layer", "geojson-source") {
                    lineColor("#00b4d8")
                    lineWidth(5.0)
                }
                , "road-intersection"
            )
        }
    }

    /**
     * This function removes the route from the map.
     */
    private fun removeRoute() {
        val mapView = _uiState.value.mapView
        mapView?.getMapboxMap()?.getStyle { style ->
            style.removeStyleLayer("geojson-layer")
            style.removeStyleSource("geojson-source")
        }
    }

    /**
     * This function converts the JSON response from the Mapbox Directions API to a GeoJSON string.
     *
     * @param jsonData the JSON response from the Mapbox Directions API
     * @return a GeoJSON
     */
    private fun convertToGeoJSON(jsonData: String): String {
        val jsonObject = JSONObject(jsonData)
        val routesArray = jsonObject.getJSONArray("routes")
        val firstRoute = routesArray.getJSONObject(0)
        val legsArray = firstRoute.getJSONArray("legs")
        val firstLeg = legsArray.getJSONObject(0)
        val stepsArray = firstLeg.getJSONArray("steps")

        val features = mutableListOf<Feature>()

        for (i in 0 until stepsArray.length()) {
            val step = stepsArray.getJSONObject(i)
            val geometry = step.getJSONObject("geometry")
            val lineString = LineString.fromJson(geometry.toString())

            features.add(Feature.fromGeometry(lineString))
        }

        val featureCollection = FeatureCollection.fromFeatures(features)
        return featureCollection.toJson()
    }


    private fun readGeoJsonFile(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
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
            locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())
        } else {
            getPermission()
        }
    }

    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    /**
     * This function is called when the user clicks on the "Get current location" button.
     * It checks if the user has granted the location permission and if so, it gets the last
     * known location. If the user has not granted the permission, it requests the permission.
     */
    fun getCurrentLocation(
        onSuccess: (location: Location) -> Unit,
        onError: (exception: Exception) -> Unit
    ) {
        if (checkPermission()) {
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Exception("No location found"))
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } else {
            getPermission()
        }
    }

    /**
     * Returns the distance between the user's location and the given location.
     * @param lat1 latitude of the given location
     * @param lon1 longitude of the given location
     * @param location user's location
     * @return returns distance in meters
     */
    fun getAirDistanceFromPosToPos(lat1: Double, lon1: Double, location: Location): Int {

        val lat2 = location.latitude
        val lon2 = location.longitude

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
    }

    /**
     * This function is called when the location is updated. It checks if the new location is
     * different enough from the last location to update the map.
     */
    override fun onLocationResult(result: LocationResult) {
        super.onLocationResult(result)

        val currentLocation = result.lastLocation
        val lastLocation = _uiState.value.lastLocation

        if (currentLocation == null) {
            Log.d("MapViewModel", "Location not updated, current location is null")
            return
        }

        val distanceToLastLocation = lastLocation?.distanceTo(currentLocation) ?: Float.MAX_VALUE

        when {
            lastLocation == null -> {
                _uiState.value = _uiState.value.copy(lastLocation = currentLocation)
                nowCastViewModel.fetchNowCastData(
                    currentLocation.latitude,
                    currentLocation.longitude
                )
                cameraToUserLocation()
                Log.d("MapViewModel", "Location updated, last location was null")
            }
            distanceToLastLocation > bigDistanceChange -> {
                _uiState.value = _uiState.value.copy(lastLocation = currentLocation)
                nowCastViewModel.fetchNowCastData(
                    currentLocation.latitude,
                    currentLocation.longitude
                )
                cameraToUserLocation()
                Log.d(
                    "MapViewModel",
                    "Location updated, distance big enough and big distance change"
                )
            }
            distanceToLastLocation > minimalDistance -> {
                _uiState.value = _uiState.value.copy(lastLocation = currentLocation)
                cameraToUserLocation()
                Log.d("MapViewModel", "Location updated, distance big enough")
            }
            else -> {
                Log.d("MapViewModel", "Location not updated, distance too small")
            }
        }
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

