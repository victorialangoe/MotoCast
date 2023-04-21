package com.example.motocast.ui.viewmodel.weather

import Data
import LongTermWeatherData
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.LocationForecastDataSource
import com.example.motocast.data.datasource.MetAlertsDataSource
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.Properties
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides the data to the NowCastScreen via the uiState variable.
 * The data is fetched every 5 minutes.
 */
class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    private var job: Job? = null
    private val nowCastDataSource = NowCastDataSource()
    private val locationForecastDataSource = LocationForecastDataSource()
    private val metAlertsDataSource = MetAlertsDataSource()

    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()


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

    /**
     * Fetches the data from the NowCastDataSource or LocationForecastDataSource depending on the time.
     * If the time is less than 2 hours from now, the NowCastDataSource is used. Otherwise the LocationForecastDataSource is used.
     * @param longitude The longitude of the location
     * @param latitude The latitude of the location
     * @param timestamp The timestamp of the time we want to get the weather data for
     */
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar,
        callback: (RouteWeatherUiState?) -> Unit
    ) {

        if (_uiState.value.alerts == null) {
            Log.d("WeatherViewModel", "Fetching alerts")
            fetchAlerts()
        }


        val hoursFromNow = calculateHoursFromNow(timestamp)
        Log.d("WeatherViewModel", "Hours from now: $hoursFromNow")
        try {
            val result = when {
                hoursFromNow < 2 -> {
                    withContext(Dispatchers.IO) {
                        fetchNowCastData(latitude, longitude)
                    }
                }
                else -> {
                    withContext(Dispatchers.IO) {
                        fetchLocationForecastData(latitude, longitude, timestamp)
                    }
                }
            }
            // Check for alerts
            val alert = checkForAlerts(latitude, longitude, timestamp)
            // Convert the result to RouteWeatherUiState
            if (result != null) {
                val routeWeatherUiState = RouteWeatherUiState(
                    isLoading = false,
                    symbolCode = result.symbolCode,
                    temperature = result.temperature,
                    windSpeed = result.windSpeed,
                    windDirection = result.windDirection,
                    error = null,
                    updatedAt = result.updatedAt,
                    alert = alert
                )
                callback(routeWeatherUiState)
            } else {
                callback(null)
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Error: $e")
            callback(null)
        }

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

    private fun setAlerts(alerts: MetAlertsDataModel) {
        _uiState.value = _uiState.value.copy(
            alerts = alerts
        )
    }

    private suspend fun fetchAlerts() {
        val response = metAlertsDataSource.getMetAlertsData()
        response?.let {
            Log.d("WeatherViewModel", "Alerts: ${it.features.size}")
            setAlerts(it)
        } ?: run {
            Log.e("WeatherViewModel", "Error: Failed to fetch alerts")
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
    ): Properties? {
        val alerts = _uiState.value.alerts
        Log.d("WeatherViewModel - Check", "Alerts: ${alerts?.features?.size}")
        if (alerts != null) {
            for (alert in alerts.features) {
                // Check if the timestamp is between the start and end time interval
                val interval = alert.`when`.interval
                val startTime = stringToCalendar(interval.first())
                val endTime = stringToCalendar(interval.last())
                if (timestamp.before(endTime) && timestamp.after(startTime)) {
                    // The timestamp is not between the start and end time interval
                    Log.d("WeatherViewModel - Check", "Timestamp is" +
                            " between start and end time")
                    alert.geometry.coordinates.map { coordinates ->
                        val jtsCoordinates = coordinates.map { coordinate ->
                            Coordinate(coordinate[0], coordinate[1])
                        }.toTypedArray()

                        val geometryFactory = GeometryFactory()
                        val polygon = geometryFactory.createPolygon(jtsCoordinates)
                        val point = geometryFactory.createPoint(Coordinate(longitude, latitude))

                        if (point.within(polygon)) {
                            Log.d("WeatherViewModel - Check", "Alert: ${alert.properties.title}")
                            return alert.properties
                        }
                    }

                } else {
                    Log.d("WeatherViewModel - Check", "Alert: ${alert.properties.title}")
                }
            }
        }
        return null
    }


    /**
     * Fetch the data from the API and update the UI.
     */
    private suspend fun fetchNowCastData(
        latitude: Double,
        longitude: Double
    ): WeatherUiState? {
        val response = nowCastDataSource.getNowCastData(latitude, longitude)

        return if (response != null) {
            val firstTimeseries = response.properties.timeseries.first().data
            WeatherUiState(
                isLoading = false,
                symbolCode = firstTimeseries.next_1_hours.summary.symbol_code,
                temperature = firstTimeseries.instant.details.air_temperature,
                windSpeed = firstTimeseries.instant.details.wind_speed,
                windDirection = firstTimeseries.instant.details.wind_from_direction,
                updatedAt = response.properties.meta.updated_at
            )
        } else {
            Log.e("NowCastViewModel", "Error: Failed to fetch data")
            null
        }
    }

    /**
     * Fetch LocationForecast data from the API based on the timestamp, latitude and longitude.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param timestamp The timestamp of the location
     * @return The weather data for the location
     * @throws Exception If the API returns an error or if the data is not found
     */
    private suspend fun fetchLocationForecastData(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar
    ): WeatherUiState {
        val response = locationForecastDataSource.getLongTermWeatherData(latitude, longitude)

        if (response != null) {
            val zeroedTimestamp = getZeroedTimestamp(timestamp)
            val data: Data? = findClosestWeatherData(response, zeroedTimestamp)

            if (data != null) {
                return WeatherUiState(
                    isLoading = false,
                    symbolCode = if (data.next_1_hours != null) {
                        data.next_1_hours.summary.symbol_code
                    } else {
                        data.next_6_hours.summary.symbol_code
                    },
                    temperature = data.instant.details.air_temperature,
                    windSpeed = data.instant.details.wind_speed,
                    windDirection = data.instant.details.wind_from_direction,
                    updatedAt = response.properties.meta.updated_at
                )
            } else {
                throw Exception("No data found for timestamp: ${formatToISO8601(zeroedTimestamp)}")
            }
        } else {
            throw Exception("Error: Failed to fetch data")
        }
    }


    /**
     * Find the closest weather data to the timestamp.
     * Sometimes the API doesn't have data for the exact timestamp, so we need to find the closest one. (00:00, 06:00, 12:00, 18:00)
     *
     * @param response The response from the API containing all the weather data
     * @param zeroedTimestamp The timestamp to find the closest data for (without minutes, seconds and milliseconds)
     * @return The closest weather data as a [Data] object or null if no data was found.
     */
    private fun findClosestWeatherData(
        response: LongTermWeatherData,
        zeroedTimestamp: Calendar
    ): Data? {
        var timeseries = response.properties.timeseries.find {
            it.time == formatToISO8601(zeroedTimestamp)
        }
        if (timeseries == null) {
            val currentHour = zeroedTimestamp.get(Calendar.HOUR_OF_DAY)
            val closestHour = listOf(0, 6, 12, 18).minByOrNull { abs(it - currentHour) }

            for (hour in closestHour!!..23) {
                zeroedTimestamp.set(Calendar.HOUR_OF_DAY, hour)
                val timestampString = formatToISO8601(zeroedTimestamp)
                Log.d(
                    "WeatherViewModel",
                    "Looking for data for the timestamp: $timestampString"
                )

                timeseries = response.properties.timeseries.find {
                    it.time == timestampString
                }
                if (timeseries != null) {
                    Log.d(
                        "WeatherViewModel",
                        "Found data for the timestamp: $timestampString"
                    )
                    break
                }
            }
            Log.e(
                "WeatherViewModel",
                "No data found for the timestamp: ${formatToISO8601(zeroedTimestamp)}"
            )
        }
        return timeseries?.data
    }

    private fun calculateHoursFromNow(timestamp: Calendar?): Double {
        return timestamp?.let {
            val timeRightNow = Calendar.getInstance()
            val diff = timestamp.timeInMillis - timeRightNow.timeInMillis
            diff.toDouble() / (60 * 60 * 1000)
        } ?: 0.0
    }

    private fun formatToISO8601(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

    /**
     * Get the timestamp with the MINUTE, SECOND and MILLISECOND set to 0.
     * @param timestamp The timestamp to zero out.
     */
    private fun getZeroedTimestamp(timestamp: Calendar): Calendar {
        val zeroedTimestamp = timestamp.clone() as Calendar
        zeroedTimestamp.set(Calendar.DAY_OF_MONTH, timestamp.get(Calendar.DAY_OF_MONTH))
        zeroedTimestamp.set(Calendar.YEAR, timestamp.get(Calendar.YEAR))
        zeroedTimestamp.set(Calendar.HOUR_OF_DAY, timestamp.get(Calendar.HOUR_OF_DAY))
        zeroedTimestamp.set(Calendar.MINUTE, 0)
        zeroedTimestamp.set(Calendar.SECOND, 0)
        zeroedTimestamp.set(Calendar.MILLISECOND, 0)
        return zeroedTimestamp
    }


}