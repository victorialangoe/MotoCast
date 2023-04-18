package com.example.motocast.ui.viewmodel.route_planner

import ReverseGeocodingResult
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.DirectionsDataSource
import com.example.motocast.data.datasource.ReverseGeocodingSource
import com.example.motocast.data.model.Leg
import com.example.motocast.data.model.RouteSearchResult
import com.example.motocast.data.model.Waypoint
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.weather.WeatherUiState
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.internal.format
import java.time.Duration
import java.util.*

class RoutePlannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoutePlannerUiState())
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    val uiState = _uiState
    private val reverseGeocodingDataSource = ReverseGeocodingSource()


    /* This method is only used for debugging purposes */
    private fun printDestinations() {
        // create a string with - between each destination
        _uiState.value.destinations.forEachIndexed { index, destination ->
            Log.d(
                "RoutePlannerViewModel", "Destination $index: ${
                    "name: ${destination.name}, " +
                            "latitude: ${destination.latitude}, " +
                            "longitude: ${destination.longitude}, " +
                            "timestamp: ${destination.timestamp}"
                }"
            )
        }
    }

    fun getTotalDestinations(): Int {
        return _uiState.value.destinations.size
    }

    fun getDestinationNamesAsString(): String {
        val currentUiState = _uiState.value
        var destinationsString = ""
        currentUiState.destinations.forEachIndexed { index, destination ->
            if (index == 0) {
                destinationsString += destination.name
            } else {
                destinationsString += " - ${destination.name}"
            }
        }
        return destinationsString
    }

    /**
     * This method adds a new destination to the list of destinations if the list is not full. (max 5 destinations)
     * Sets the active destination to the last destination in the list. (the one that was just added)
     * Navigates to the add destination screen. (the screen where the user can add a destination)
     */
    fun addDestination(navigateTo: () -> Unit) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        // max 5 destinations
        if (newDestinations.size < 5) {
            // insert new destination before the last item
            val lastDestinationIndex = newDestinations.lastIndex
            newDestinations.add(lastDestinationIndex, Destination(null, 0.0, 0.0, 0))
            _uiState.value = currentUiState.copy(destinations = newDestinations)
            // set active destination to the new destination
            setActiveDestinationIndex(lastDestinationIndex)
            // Navigate to add destination screen
            navigateTo()
        }
    }


    fun editDestination(index: Int, navigateTo: () -> Unit) {
        setActiveDestinationIndex(index)
        navigateTo()
    }

    fun setActiveDestinationIndex(index: Int) {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(activeDestinationIndex = index)
        printDestinations()
    }

    fun updateDestination(index: Int, address: Address) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        val destination = newDestinations[index]
        newDestinations[index] = destination.copy(
            name = address.addressText,
            latitude = address.latitude,
            longitude = address.longitude,
        )
        _uiState.value = currentUiState.copy(destinations = newDestinations)

        printDestinations()
    }

    fun removeDestination(index: Int) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        Log.d("RoutePlannerViewModel", "Removing destination at index $index")
        Log.d("RoutePlannerViewModel", "Current destinations: ${newDestinations.size}")
        // min 2 destinations
        if (newDestinations.size > 2) {
            newDestinations.removeAt(index)
            _uiState.value = currentUiState.copy(destinations = newDestinations)
            printDestinations()
        }
    }

    fun updateStartTime(time: Calendar){
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(startTime = time)
    }

    /**
     * Clears all destinations and resets the start timestamp
     */
    fun clear() {
        _uiState.value = RoutePlannerUiState()
        _uiState.value = _uiState.value.copy(startTime = Calendar.getInstance())
        printDestinations()

    }

    fun checkIfAllDestinationsHaveNames(): Boolean {
        val currentUiState = _uiState.value
        currentUiState.destinations.forEach {
            if (it.name == null || it.name == "") {
                return false
            }
        }
        return true
    }

    /**
     * This method returns the start date in the format: MM-DD
     */
    fun getStartDate(): String {
        val startTime = _uiState.value.startTime
        val month = startTime.get(Calendar.MONTH) + 1
        val day = startTime.get(Calendar.DAY_OF_MONTH)
        // add 0 in front of month and day if they are less than 10
        return format("%02d-%02d", month, day)
    }


    /**
     * This method returns the start time in the format: HH:mm
     */
    fun getStartTime(): String {
        val startTime = _uiState.value.startTime
        val hour = startTime.get(Calendar.HOUR_OF_DAY)
        val minute = startTime.get(Calendar.MINUTE)
        // add 0 in front of hour and minute if they are less than 10
        return format("%02d:%02d", hour, minute)
    }


    /**
     * This method returns the duration of the trip. // TODO: remove dummy value
     */
    fun getDuration(seconds: Long = 500): String {

        val duration = Duration.ofSeconds(seconds)
        return "Varighet: " + when {
            duration.toDays() > 0 -> "${duration.toDays()} ${if (duration.toDays() == 1L) "dag" else "dager"}"
            duration.toHours() > 0 -> "${duration.toHours()} ${if (duration.toHours() == 1L) "time" else "timer"}"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} min"
            else -> "$seconds sek"
        }
    }

    private fun getDestinationsCoordinatesAsString(): String {
        val currentUiState = _uiState.value
        val destinations = currentUiState.destinations
        val coordinates = mutableListOf<String>()
        destinations.forEach {
            coordinates.add("${it.longitude},${it.latitude}")
        }
        return coordinates.joinToString(";")
    }

    /**
     * This function converts the JSON response from the Mapbox Directions API to a GeoJSON string.
     *
     * @param routeSearchResult the response from the Mapbox Directions API
     * @return a GeoJSON string
     */
    private fun convertRouteSearchResultToGeoJSON(routeSearchResult: RouteSearchResult): String {

        val routesArray = routeSearchResult.routes
        val firstRoute = routesArray[0]
        val legsArray = firstRoute.legs
        val features = mutableListOf<Feature>()

        for (legIndex in legsArray.indices) {
            val currentLeg = legsArray[legIndex]
            val stepsArray = currentLeg.steps

            for (stepIndex in stepsArray.indices) {
                val step = stepsArray[stepIndex]
                val geometry = step.geometry
                // Convert the list of coordinates to a list of Point objects
                val coordinatesList = geometry.coordinates.map { Point.fromLngLat(it[0], it[1]) }
                // Create a LineString from the list of Point objects
                val lineString = LineString.fromLngLats(coordinatesList)

                features.add(Feature.fromGeometry(lineString))
            }
        }
        val featureCollection = FeatureCollection.fromFeatures(features)

        return featureCollection.toJson()
    }

    private fun addGeoJsonDataToUiState(geoJsonData: String) {
        _uiState.value = _uiState.value.copy(geoJsonData = geoJsonData)
    }


    private suspend fun addWaypointsToUiState(
        legs: List<Leg>,
        waypoints: List<Waypoint>,
        startTime: Calendar
    ) = coroutineScope {
        val weatherViewModel = WeatherViewModel()
        val routeWithWaypoint =
            createRouteWithWaypoints(waypoints, weatherViewModel, startTime)

        updateRouteDurations(legs, routeWithWaypoint)
        updateRouteTimeStamps(legs, routeWithWaypoint, startTime)

        _uiState.value = _uiState.value.copy(waypoints = routeWithWaypoint)
    }


    private fun updateRouteDurations(
        legs: List<Leg>,
        routeWithWaypoint: MutableList<RouteWithWaypoint>
    ) {
        // Update the durations of the routes
        for (legIndex in legs.indices) {
            val leg = legs[legIndex]
            val route = routeWithWaypoint[legIndex + 1]
            val updatedRoute = route.copy(
                timeFromStart = leg.duration)
            routeWithWaypoint[legIndex + 1] = updatedRoute
        }
    }

    private fun updateRouteTimeStamps(
        legs: List<Leg>,
        routeWithWaypoint: MutableList<RouteWithWaypoint>,
        startTime: Calendar
    ) {
        // Update the timestamps of the routes
        var timeFromStart = 0.0
        for (legIndex in legs.indices) {
            val leg = legs[legIndex]
            val route = routeWithWaypoint[legIndex + 1]
            timeFromStart += leg.duration
            val updatedRoute = route.copy(
                timestamp = startTime.apply {
                    add(Calendar.SECOND, timeFromStart.toInt())
                })
            routeWithWaypoint[legIndex + 1] = updatedRoute
        }
    }

    private suspend fun createRouteWithWaypoints(
        waypoints: List<Waypoint>,
        weatherViewModel: WeatherViewModel,
        startTime: Calendar,
    ): MutableList<RouteWithWaypoint> {

        return coroutineScope {

            val deferredRoutes = waypoints.mapIndexed { index, waypoint ->
                async {

                    val timestamp = startTime.apply {
                        add(Calendar.SECOND, index * 60)
                    }

                    val name = getReverseGeocodedName(
                        longitude = waypoint.location[0],
                        latitude = waypoint.location[1]
                    )

                    val route = RouteWithWaypoint(
                        name = name ?: "Ukjent destinasjon",
                        longitude = waypoint.location[0],
                        latitude = waypoint.location[1],
                        timestamp = timestamp,
                        timeFromStart = null,
                    )

                    val weatherUiState = route.getWeatherData(weatherViewModel)
                    val updatedRoute = route.copy(weatherUiState = weatherUiState)
                    updatedRoute
                }
            }
            deferredRoutes.awaitAll().toMutableList()
        }
    }

    private suspend fun RouteWithWaypoint.getWeatherData(weatherViewModel: WeatherViewModel): WeatherUiState? {
        return if (latitude != null && longitude != null && timestamp != null) {
            weatherViewModel.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                timestamp = timestamp
            )
        } else null
    }

    suspend fun getReverseGeocodedName(longitude: Double, latitude: Double): String? {
        val nameDeferred = CompletableDeferred<String?>()

        reverseGeocodingDataSource.getReverseGeocodingData(
            longitude,
            latitude,
            onSuccess = { response: ReverseGeocodingResult ->
                var name = response.features.firstOrNull()?.placeName
                name = name?.replace(", Norge", "") ?: name
                nameDeferred.complete(name)
            },
            onError = { error: String ->
                Log.e("RouteViewModel", error)
                nameDeferred.complete(null)
            }
        )

        return nameDeferred.await()
    }



    /**
     * Starts the route planning
     */
    fun start(navigateTo: () -> Unit, fitCameraToRouteAndWaypoints: () -> Unit) {

        val directionsDataSource = DirectionsDataSource()

        val currentUiState = _uiState.value
        if (!checkIfAllDestinationsHaveNames()) {
            _uiState.value = currentUiState.copy(error = "All destinations must have a name")
            return
        }

        // removes null to avoid null pointer exception
        val destinations = currentUiState.destinations

        //needs 2 destinations to get a route
        if (destinations.size >= 2) {
            directionsDataSource.getDirectionsData(
                getDestinationsCoordinatesAsString(),
                onSuccess = { routeSearchResult: RouteSearchResult ->
                    viewModelScope.launch {
                        val geoJsonData = convertRouteSearchResultToGeoJSON(routeSearchResult)


                        withContext(Dispatchers.Main) {
                            addGeoJsonDataToUiState(geoJsonData)
                            fitCameraToRouteAndWaypoints()

                            val waypoints = routeSearchResult.waypoints
                            val legs = routeSearchResult.routes[0].legs
                            addWaypointsToUiState(
                                legs,
                                waypoints,
                                startTime = _uiState.value.startTime
                            )
                            Log.d(
                                "RoutePlannerViewModel",
                                "Uistate waypoints: ${_uiState.value.waypoints}"
                            )
                        }
                    }
                },
                onError = { error: String ->
                    Log.d("RoutePlannerViewModel", "Error: $error")
                })

        }

        navigateTo()
        printDestinations()
    }
}

