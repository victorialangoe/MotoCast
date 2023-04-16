package com.example.motocast.ui.viewmodel.route_planner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.directions.DirectionsHelper
import com.example.motocast.ui.viewmodel.address.Address
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.util.*

class RoutePlannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoutePlannerUiState())

    val uiState = _uiState

    init {
        setCurrentTimeAndDate()
    }

    private fun setCurrentTimeAndDate(){
        // get time and date from system
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        // update ui state
        _uiState.value = _uiState.value.copy(
            startTime = TimeAndDateUiState(
                TimePickerUiState(hour, minute),
                DatePickerUiState(year, month, dayOfMonth)
            )
        )
    }

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


    fun editDestination(index: Int, navigateTo:() -> Unit) {
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

    fun updateDateUiState(datePickerUiState: DatePickerUiState) {
        val currentUiState = _uiState.value
        val newTimeAndDateUiState = currentUiState.startTime.copy(
            datePickerUiState = datePickerUiState
        )
        _uiState.value = currentUiState.copy(startTime = newTimeAndDateUiState)
        printDestinations()
    }

    fun updateTimeUiState(timePickerUiState: TimePickerUiState) {
        val currentUiState = _uiState.value
        val newTimeAndDateUiState = currentUiState.startTime.copy(
            timePickerUiState = timePickerUiState
        )
        _uiState.value = currentUiState.copy(startTime = newTimeAndDateUiState)
        printDestinations()
    }

    /**
     * Clears all destinations and resets the start timestamp
     */
    fun clear() {
        _uiState.value = RoutePlannerUiState()
        setCurrentTimeAndDate()
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
        val currentUiState = _uiState.value
        val timeAndDateUiState = currentUiState.startTime
        val datePickerUiState = timeAndDateUiState.datePickerUiState
        val month = if (datePickerUiState.month < 10) {
            "0${datePickerUiState.month}"
        } else {
            datePickerUiState.month.toString()
        }
        val day = if (datePickerUiState.day < 10) {
            "0${datePickerUiState.day}"
        } else {
            datePickerUiState.day.toString()
        }
        return "$month-$day"
    }


    /**
     * This method returns the start time in the format: HH:mm
     */
    fun getStartTime(): String {
        val currentUiState = _uiState.value
        val timeAndDateUiState = currentUiState.startTime
        val timePickerUiState = timeAndDateUiState.timePickerUiState
        val hour = if (timePickerUiState.hour < 10) {
            "0${timePickerUiState.hour}"
        } else {
            timePickerUiState.hour.toString()
        }
        val minute = if (timePickerUiState.minute < 10) {
            "0${timePickerUiState.minute}"
        } else {
            timePickerUiState.minute.toString()
        }
        return "$hour:$minute"
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


    suspend fun getRoute(destinations: List<Destination>, accessToken: String) {
        val coordinates = destinations.joinToString(separator = ";") { destination ->
            "${destination.longitude},${destination.latitude}"
        }




        val directionsHelper = DirectionsHelper()
        val service = directionsHelper.createDirectionsAPI() ?: return
        val call = service.getDirections(
            coordinates = coordinates,
            accessToken = accessToken,
            alternatives = false,
            geometries = "geojson",
            language = "en",
            overview = "simplified",
            steps = true
        )

        withContext(Dispatchers.IO) {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let { responseBody ->
                            val jsonResponse = responseBody.string()
                            convertToGeoJSON(jsonResponse)
                        }
                    } else {
                        Log.e(
                            "MapActivity",
                            "Error getting route: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("MapActivity", "Error getting route: ${t.message}")
                }
            })
        }
    }

    /**
     * This function converts the JSON response from the Mapbox Directions API to a GeoJSON string.
     *
     * @param jsonData the JSON response from the Mapbox Directions API
     * @return a GeoJSON
     */
    private fun convertToGeoJSON(jsonData: String) {
        val jsonObject = JSONObject(jsonData)
        val routesArray = jsonObject.getJSONArray("routes")
        val firstRoute = routesArray.getJSONObject(0)
        val legsArray = firstRoute.getJSONArray("legs")

        val features = mutableListOf<Feature>()

        for (legIndex in 0 until legsArray.length()) {
            val currentLeg = legsArray.getJSONObject(legIndex)
            val stepsArray = currentLeg.getJSONArray("steps")

            for (stepIndex in 0 until stepsArray.length()) {
                val step = stepsArray.getJSONObject(stepIndex)
                val geometry = step.getJSONObject("geometry")
                val lineString = LineString.fromJson(geometry.toString())

                features.add(Feature.fromGeometry(lineString))
            }
        }

        val featureCollection = FeatureCollection.fromFeatures(features)
        _uiState.value = _uiState.value.copy(geoJsonData = featureCollection.toJson())
    }

    /**
     * Starts the route planning
     */
    fun start(navigateTo: () -> Unit, fitCameraToRouteAndWaypoints: ()->Unit) {
        val currentUiState = _uiState.value
        if (!checkIfAllDestinationsHaveNames()) {
            _uiState.value = currentUiState.copy(error = "All destinations must have a name")
            return
        }

        // removes null to avoid null pointer exception
        val destinations = currentUiState.destinations.filterNotNull()

        //needs 2 destinations to get a route
        if (destinations.size >= 2) {
            viewModelScope.launch {
                getRoute(destinations, BuildConfig.MAPBOX_ACCESS_TOKEN)
                fitCameraToRouteAndWaypoints()
            }
        }
        navigateTo()
        printDestinations()
    }
}

