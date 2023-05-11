package com.example.motocast.ui.viewmodel.route_planner

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.R
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.Leg
import com.example.motocast.data.model.Waypoint
import com.example.motocast.domain.use_cases.FetchDirectionsDataUseCase
import com.example.motocast.domain.use_cases.GetGeocodedNameUseCase
import com.example.motocast.domain.use_cases.GetResourcesUseCase
import com.example.motocast.domain.use_cases.GetWeatherDataUseCase
import com.example.motocast.domain.utils.Utils
import com.example.motocast.domain.utils.Utils.checkIfTimeIsMoreThan8DaysInFuture
import com.example.motocast.domain.utils.Utils.formatDate
import com.example.motocast.domain.utils.Utils.formatDurationAsTimeString
import com.example.motocast.domain.utils.Utils.formatTime
import com.example.motocast.ui.viewmodel.address.Address
import com.mapbox.common.location.Location
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject


@HiltViewModel
class RoutePlannerViewModel @Inject constructor(
    private val getGeocodedNameUseCase: GetGeocodedNameUseCase,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getDirectionsDataUseCase: FetchDirectionsDataUseCase,
    private val getResourcesUseCase: GetResourcesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutePlannerUiState())
    val uiState = _uiState.asStateFlow()

    fun getTotalDestinations(): Int {
        return _uiState.value.destinations.size
    }

    private fun updateUiState(update: (RoutePlannerUiState) -> RoutePlannerUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun editDestination(index: Int, navigateTo: () -> Unit) {
        setActiveDestinationIndex(index)
        navigateTo()
    }

    fun setActiveDestinationIndex(index: Int) {
        updateUiState { it.copy(activeDestinationIndex = index) }
    }

    fun updateStartTime(time: Calendar) {
        if (checkIfTimeIsMoreThan8DaysInFuture(time)) {
            updateUiState { it.copy(startTime = Calendar.getInstance()) }
        } else {
            updateUiState { it.copy(startTime = time) }
        }
    }

    fun clear() {
        _uiState.value = RoutePlannerUiState()
        updateUiState { it.copy(startTime = Calendar.getInstance()) }
    }

    private fun addGeoJsonDataToUiState(geoJsonData: String) {
        _uiState.value = _uiState.value.copy(geoJsonData = geoJsonData)
    }

    fun checkIfAllDestinationsHaveNames(): Boolean {
        return Utils.checkIfAllDestinationsHaveNames(_uiState.value.destinations)
    }

    fun checkIfSomeDestinationsHaveNames(): Boolean {
        return Utils.checkIfSomeDestinationsHaveNames(_uiState.value.destinations)
    }

    fun getStartDate(): String {
        return formatDate(_uiState.value.startTime)
    }

    fun getStartTime(): String {
        return formatTime(_uiState.value.startTime)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = R.string.empty_string)
    }

    private fun getDurationAsString(durationInSek: Long): String {
        val resource = getResourcesUseCase()
        return formatDurationAsTimeString(durationInSek, resource)
    }

    private fun getDestinationsCoordinatesAsString(): String {
        val currentUiState = _uiState.value
        val destinations = currentUiState.destinations
        val coordinates = mutableListOf<String>()
        destinations.forEach {
            if (it.latitude != null && it.longitude != null) {
                val location: Location =
                    Location.Builder().latitude(it.latitude).longitude(it.longitude).build()
                coordinates.add("${location.longitude},${location.latitude}")
            }
        }
        return coordinates.joinToString(";")
    }

    fun updateDestination(index: Int, address: Address) {
        val newDestinations = _uiState.value.destinations.toMutableList()
        val destination = newDestinations[index]
        newDestinations[index] = destination.copy(
            name = address.addressText,
            latitude = address.latitude,
            longitude = address.longitude,
        )
        updateUiState { it.copy(destinations = newDestinations) }
    }

    fun removeDestination(index: Int) {
        val currentUiState = _uiState.value
        val newDestinations = currentUiState.destinations.toMutableList()
        // min 2 destinations
        if (newDestinations.size > 2) {
            newDestinations.removeAt(index)
            updateUiState { it.copy(destinations = newDestinations) }
        }
    }

    /**
     * Adds a new destination to the list of destinations
     * @param navigateTo The function to call to navigate to the destination editor
     */
    fun addDestination(navigateTo: () -> Unit) {
        val newDestinations = _uiState.value.destinations.toMutableList()

        if (newDestinations.size < 10) {
            val lastDestinationIndex = newDestinations.lastIndex

            newDestinations.add(lastDestinationIndex, Destination(null, 0.0, 0.0, 0))

            updateUiState { it.copy(destinations = newDestinations) }

            setActiveDestinationIndex(lastDestinationIndex)

            navigateTo()
        }
    }


    /**
     * This function converts the JSON response from the Mapbox Directions API to a GeoJSON string.
     *
     * @param directionsDataModel The JSON response from the Mapbox Directions API
     * @return A GeoJSON string
     */
    private fun convertRouteSearchResultToGeoJSON(directionsDataModel: DirectionsDataModel): String {

        val routesArray = directionsDataModel.routes
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


    private suspend fun addWaypointsToUiState(
        legs: List<Leg>,
        waypoints: List<Waypoint>,
        startTime: Calendar,
    ) {

        val routeWithWaypoint = createRouteWithWaypoints(waypoints, startTime)
        updateTimeStampForLegs(legs, routeWithWaypoint, startTime)
        val additionalWaypoints = addWaypointsOnLegs(legs)
        routeWithWaypoint.addAll(additionalWaypoints)

        updateRouteTimeStamps(routeWithWaypoint, startTime)
        updateRouteWeather(routeWithWaypoint)
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
    ) {

        for (routeIndex in routeWithWaypoint.indices) {
            val route = routeWithWaypoint[routeIndex]

            if (route.latitude != null && route.longitude != null && route.timestamp != null) {

                val weatherData = getWeatherDataUseCase(
                    route.latitude,
                    route.longitude,
                    route.timestamp
                )

                val updatedRoute = route.copy(
                    weather = weatherData
                )

                routeWithWaypoint[routeIndex] = updatedRoute
            }
        }
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
                    val name = getGeocodedNameUseCase(
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

    suspend fun start(
        navigateTo: () -> Unit,
        fitCameraToRouteAndWaypoints: () -> Unit,
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true)


        if (!checkIfAllDestinationsHaveNames()) {
            _uiState.value = _uiState.value.copy(error = R.string.all_destinations_must_be_filled_error)
            return
        }

        if (_uiState.value.destinations.size >= 2) {

            var response = getDirectionsDataUseCase(

                getDestinationsCoordinatesAsString()
            )
            if (response?.code == "NoRoute") {
                _uiState.value = _uiState.value.copy(
                    error = R.string.route_not_found_error,
                    isLoading = false,
                )
                return
            }

            Log.d("start", "response: $response")

            if (response != null) {

                val geoJsonData = convertRouteSearchResultToGeoJSON(response)

                withContext(Dispatchers.Main) {
                    addGeoJsonDataToUiState(geoJsonData)
                    fitCameraToRouteAndWaypoints()

                    val waypoints = response.waypoints
                    val legs = response.routes[0].legs
                    val duration = response.routes[0].duration
                    _uiState.value = _uiState.value.copy(
                        durationAsString = "Ruten varer i " + getDurationAsString(duration.toLong())
                    )

                    addWaypointsToUiState(
                        legs,
                        waypoints,
                        startTime = _uiState.value.startTime,
                    )
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        } else {
            _uiState.value = _uiState.value.copy(
                error = R.string.need_2_destination_error,
                isLoading = false
            )
        }
        navigateTo()
    }
}
