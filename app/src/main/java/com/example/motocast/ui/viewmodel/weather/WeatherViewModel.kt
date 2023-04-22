package com.example.motocast.ui.viewmodel.weather

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.model.Properties
import com.example.motocast.domain.use_cases.FetchLocationForecastDataUseCase
import com.example.motocast.domain.use_cases.FetchNowCastDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
    private val fetchLocationForecastDataUseCase: FetchLocationForecastDataUseCase,
) :
ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    private var job: Job? = null

    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private suspend fun fetchNowCastData(
        latitude: Double,
        longitude: Double
    ): WeatherUiState? {

        return fetchNowCastDataUseCase(latitude, longitude)
    }

    private suspend fun fetchLocationForecastData(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar
    ): WeatherUiState? {
        return fetchLocationForecastDataUseCase(
            latitude,
            longitude,
            timestamp
        )
    }

    /**
     * Start fetching the data every 5 minutes, but tries to fetch every 10 sec
     *
     * We only want to fetch the data if the user is in the area where the API (Scandinavia) is valid.
     *  Maximum Latitude: 71.18°N (Nordkinn Peninsula, Norway)
     *  Minimum Latitude: 54.50°N (Kattegat, Denmark/Sweden)
     *  Maximum Longitude: 31.10°E (Varangerfjord, Norway)
     *  Minimum Longitude: 0.10°E (Skagen, Denmark)
     */
    fun startFetchingNowCastData(
        getCurrentLocation: () -> Location?,
    ) {
        Log.d("NowCastViewModel", "Start fetching the data")
        job = CoroutineScope(Dispatchers.IO).launch {
            var time =
                0 // 1 = 10 seconds, 2 = 20 seconds, 3 = 30 seconds, 4 = 40 seconds, 5 = 50 seconds, 6 = 60 seconds
            while (isActive) { // Use a loop to keep fetching the data every 5 minutes
                time += 1
                val location = getCurrentLocation()
                if (location != null) {
                    // Check if the user is in Scandinavia
                    if ((location.latitude in (54.50..71.18)) && (location.longitude in (0.10..31.10))) {
                        if (time == 6 * 5) { // Fetch the data every 5 minutes
                            updateNowCastData(location.latitude, location.longitude)
                            time = 0
                        } else if (_uiState.value.temperature == null) { // Fetch the data if the user just opened the app
                            updateNowCastData(location.latitude, location.longitude)
                        }

                    } else {
                        Log.e("NowCastViewModel", "User is not in Scandinavia")
                    }

                } else {
                    Log.e("NowCastViewModel", "User location is null")
                }

                delay(1000 * 10) // Wait 10 seconds before trying again
            }
        }
    }

    /**
     * Stop fetching the data.
     * This method should be called when the user leaves the app or the screen.
     */
    fun stopFetchingNowCastData() {
        job?.cancel() // Cancel the job to stop fetching the data
        job = null
    }


    private fun updateNowCastData(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = fetchNowCastData(latitude, longitude)
                if (result != null) {
                    _uiState.value = result
                }
            } catch (e: Exception) {
                Log.e("NowCastViewModel", "Error: $e")
            }
        }
    }


    private fun stringToCalendar(inputString: String): Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date = format.parse(inputString)
        return Calendar.getInstance().apply {
            time = date
        }
    }

    private fun checkForAlerts(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar
    ): List<Properties>? {
       val alerts = _uiState.value.alerts
        val alertsFound = mutableListOf<Properties>()
        if (alerts != null) {
        for (alert in alerts.features) {
        // Check if the timestamp is between the start and end time interval
        val interval = alert.`when`.interval
        val startTime = stringToCalendar(interval.first())
        val endTime = stringToCalendar(interval.last())
        if (timestamp.before(endTime) && timestamp.after(startTime)) {
        // The timestamp is not between the start and end time interval
        Log.d(
        "WeatherViewModel - Check", "Timestamp is" +
        " between start and end time"
        )
        alert.geometry.coordinates.map { coordinates ->
        val jtsCoordinates = coordinates.map { coordinate ->
        Coordinate(coordinate[0], coordinate[1])
        }.toTypedArray()

        val geometryFactory = GeometryFactory()
        val polygon = geometryFactory.createPolygon(jtsCoordinates)
        val point = geometryFactory.createPoint(Coordinate(longitude, latitude))

        if (point.within(polygon)) {
        alertsFound.add(alert.properties)
        }
        }

        } else {
        Log.d("WeatherViewModel - Check", "Alert: ${alert.properties.title}")
        }
        }
        }
        return alertsFound

    }








}