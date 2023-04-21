package com.example.motocast.ui.viewmodel.route_planner

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.DirectionsDataSource
import com.example.motocast.data.datasource.ReverseGeocodingSource
import com.example.motocast.data.model.Leg
import com.example.motocast.data.model.RouteSearchResult
import com.example.motocast.data.model.Waypoint
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.weather.RouteWeatherUiState
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.format
import java.time.Duration
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToLong


class RoutePlannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoutePlannerUiState())

    val uiState = _uiState.asStateFlow()
    private val reverseGeocodingDataSource = ReverseGeocodingSource()
    private val directionsDataSource = DirectionsDataSource()


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
     * This method adds a new destination to the list of destinations if the list is not full. (max 10 destinations)
     * Sets the active destination to the last destination in the list. (the one that was just added)
     * Navigates to the add destination screen. (the screen where the user can add a destination)
     */
    fun addDestination(navigateTo: () -> Unit) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        // max 5 destinations
        if (newDestinations.size < 10) {
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
        }
    }

    fun updateStartTime(time: Calendar) {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(startTime = time)
        // TODO: only 9 days in the future is allowed
    }

    /**
     * Clears all destinations and resets the start timestamp
     */
    fun clear() {
        _uiState.value = RoutePlannerUiState()
        _uiState.value = _uiState.value.copy(startTime = Calendar.getInstance())
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

    fun checkIfSomeDestinationsHaveNames(): Boolean {
        val currentUiState = _uiState.value
        currentUiState.destinations.forEach {
            if (it.name != null && it.name != "") {
                return true
            }
        }
        return false
    }

    /**
     * This method returns the start date in the format: dd MMMM
     */
    fun getStartDate(): String {
        val startTime = _uiState.value.startTime
        return (
                startTime.get(Calendar.DAY_OF_MONTH).toString() + " " +
                        startTime.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            Locale.getDefault()
                        )
                )
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
    private fun getDurationAsString(durationInSek: Long): String {
        val duration = Duration.ofSeconds(durationInSek)
        // 1 dag og 2 timer, 3 minutter og 4 sekunder

        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        return if (days > 0) {
            days.toString() + " dag" + (if (days > 1) "er" else "") + " og " +
                    hours.toString() + " time" + (if (hours > 1) "r" else "") + " og " +
                    minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
        } else {
            if (hours > 0) {
                hours.toString() + " time" + (if (hours > 1) "r" else "") + " og " +
                        minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
            } else {
                minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
            }
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
                val coordinatesList =
                    geometry.coordinates.map { Point.fromLngLat(it[0], it[1]) }
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
        startTime: Calendar,
        getWeatherData: (latitude: Double, longitude: Double, timestamp: Calendar, callback: (routeWeatherUiState: RouteWeatherUiState) -> Unit) -> Unit,

        ) = coroutineScope {

        val routeWithWaypoint = createRouteWithWaypoints(waypoints, startTime)
        updateTimeStampForLegs(legs, routeWithWaypoint, startTime)
        val additionalWaypoints = addWaypointsOnLegs(legs)
        routeWithWaypoint.addAll(additionalWaypoints)

        updateRouteTimeStamps(routeWithWaypoint, startTime)
        updateRouteWeather(routeWithWaypoint, getWeatherData)
        // sort the routes by start time
        routeWithWaypoint.sortBy {
            it.timeFromStart
        }

        _uiState.value = _uiState.value.copy(waypoints = routeWithWaypoint)
    }

    private fun updateTimeStampForLegs(
        legs: List<Leg>,
        routeWithWaypoint: MutableList<RouteWithWaypoint>,
        startTime: Calendar
    ) {
        // Set the first
        routeWithWaypoint[0] = routeWithWaypoint[0].copy(
            isInDestination = true,
        )

        // Update the timestamps of the routes
        var timeFromStart = 0.0
        for (legIndex in legs.indices) {
            val leg = legs[legIndex]
            val route = routeWithWaypoint[legIndex + 1]
            timeFromStart += leg.duration
            val time = startTime.clone() as Calendar
            time.add(Calendar.SECOND, timeFromStart.toInt())
            val updatedRoute = route.copy(
                timeFromStart = timeFromStart,
                isInDestination = true,
            )
            routeWithWaypoint[legIndex + 1] = updatedRoute
        }
    }


    private suspend fun updateRouteWeather(
        routeWithWaypoint: MutableList<RouteWithWaypoint>,
        getWeatherData: (latitude: Double, longitude: Double, timestamp: Calendar, callback: (routeWeatherUiState: RouteWeatherUiState) -> Unit) -> Unit,
    ) = coroutineScope {
        val updateJobs = mutableListOf<Job>()

        for (routeIndex in routeWithWaypoint.indices) {
            val route = routeWithWaypoint[routeIndex]

            if (route.latitude != null && route.longitude != null && route.timestamp != null) {
                val job = launch {
                    suspendCoroutine<Unit> { continuation ->
                        getWeatherData(
                            route.latitude,
                            route.longitude,
                            route.timestamp
                        ) { weather ->
                            val updatedRoute = route.copy(
                                weather = weather
                            )
                            routeWithWaypoint[routeIndex] = updatedRoute
                            continuation.resume(Unit)
                        }
                    }
                }
                updateJobs.add(job)
            } else {
                Log.d("Weather", "Weather data not received")
            }
        }

        updateJobs.forEach { it.join() }
    }

    private suspend fun addWaypointsOnLegs(
        legs: List<Leg>,
    ): MutableList<RouteWithWaypoint> {
        // Update the timestamps of the routes
        var timeFromStart = 0.0
        var timeCounter = 0.0
        val hourInSeconds = 3600.0
        val tempRoutes = mutableListOf<RouteWithWaypoint>()

        for (legIndex in legs.indices) {
            for (stepIndex in legs[legIndex].steps.indices) {
                val step = legs[legIndex].steps[stepIndex]

                timeFromStart += step.duration
                timeCounter += step.duration
                if (timeCounter > hourInSeconds) {
                    val name = getReverseGeocodedName(
                        latitude = step.maneuver.location[1],
                        longitude = step.maneuver.location[0]
                    )
                    timeCounter = 0.0
                    val newRoute = RouteWithWaypoint(
                        name = name,
                        timeFromStart = timeFromStart,
                        weather = null,
                        timestamp = null,
                        latitude = step.maneuver.location[1],
                        longitude = step.maneuver.location[0],
                    )
                    Log.d("addWaypointsOnLegs", "newRoute: $newRoute")
                    tempRoutes.add(newRoute)
                }
            }
        }
        return tempRoutes
    }


    private fun updateRouteTimeStamps(
        routeWithWaypoint: MutableList<RouteWithWaypoint>,
        startTime: Calendar
    ) {
        // Update the timestamps of the routes
        for (waypointIndex in routeWithWaypoint.indices) {
            val waypoint = routeWithWaypoint[waypointIndex]

            val timestamp = startTime.clone() as Calendar
            timestamp.add(Calendar.SECOND, waypoint.timeFromStart?.toInt() ?: 0)
            val updatedRoute = waypoint.copy(
                timestamp = timestamp,
            )
            routeWithWaypoint[waypointIndex] = updatedRoute
        }
    }

    private suspend fun createRouteWithWaypoints(
        waypoints: List<Waypoint>,
        startTime: Calendar,
    ): MutableList<RouteWithWaypoint> {

        return coroutineScope {
            val deferredRoutes = waypoints.mapIndexed { index, waypoint ->
                async {



                    val route = RouteWithWaypoint(
                        name = _uiState.value.destinations[index].name,
                        longitude = waypoint.location[0],
                        latitude = waypoint.location[1],
                        timestamp = if (index == 0) startTime else null,
                        timeFromStart = 0.0,
                    )

                    route
                }
            }
            deferredRoutes.awaitAll().toMutableList()
        }
    }

    private suspend fun getReverseGeocodedName(longitude: Double, latitude: Double): String? {
        val response = reverseGeocodingDataSource.getReverseGeocodingData(longitude, latitude)

        return if (response != null) {
            Log.d("RouteViewModel", "Reverse geocoding response: ${response.features}")
            val name = response.features.firstOrNull()?.placeName
            name?.replace(", Norge", "") ?: name
        } else {
            throw Exception("Reverse geocoding response is null")
        }
    }


    /**
     * Starts the route planning
     */
    suspend fun start(
        navigateTo: () -> Unit,
        fitCameraToRouteAndWaypoints: () -> Unit,
        getWeatherData: (latitude: Double, longitude: Double, timestamp: Calendar, callback: (routeWeatherUiState: RouteWeatherUiState) -> Unit) -> Unit,
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true)


        if (!checkIfAllDestinationsHaveNames()) {
            _uiState.value = _uiState.value.copy(error = "All destinations must have a name")
            return
        }

        if (_uiState.value.destinations.size >= 2) {

            val response = directionsDataSource.getDirectionsData(
                getDestinationsCoordinatesAsString()
            )
            if (response != null) {

                // Set loading to true
                val geoJsonData = convertRouteSearchResultToGeoJSON(response)

                withContext(Dispatchers.Main) {
                    addGeoJsonDataToUiState(geoJsonData)
                    fitCameraToRouteAndWaypoints()

                    val waypoints = response.waypoints
                    val legs = response.routes[0].legs
                    val duration = response.routes[0].duration
                    _uiState.value = _uiState.value.copy(
                        durationAsString = getDurationAsString(duration.toLong())
                    )

                    addWaypointsToUiState(
                        legs,
                        waypoints,
                        startTime = _uiState.value.startTime,
                        getWeatherData = getWeatherData
                    )
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        } else {
            _uiState.value = _uiState.value.copy(
                error = "You need at least two destinations",
                isLoading = false
            )
        }
        navigateTo()
    }
}
