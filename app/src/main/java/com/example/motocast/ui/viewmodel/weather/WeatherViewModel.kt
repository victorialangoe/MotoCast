package com.example.motocast.ui.viewmodel.weather

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.LocationForecastDataSource
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.ui.viewmodel.route_planner.RouteWithWaypoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Math.abs
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Provides the data to the NowCastScreen via the uiState variable.
 * The data is fetched every 5 minutes.
 */
class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    private var job: Job? = null
    private val nowCastDataSource = NowCastDataSource()
    private val locationForecastDataSource = LocationForecastDataSource()

    val uiState: StateFlow<WeatherUiState> = _uiState

    suspend fun getWeatherData(route: RouteWithWaypoint, startTime: Calendar): WeatherUiState? {
        return try {

            if (route.latitude == null || route.longitude == null) {
                Log.e("WeatherViewModel", "Latitude or longitude is null")
                return null
            }
            val timestamp = route.timestamp ?: startTime
            Log.d("WeatherViewModel", "Timestamp: $timestamp")
            Log.d("WeatherViewModel", "Start time: $startTime")
            // TODO: We can not fetch data from back in time, so we need to check if the timestamp is in the past
            val hoursFromNow = if (route.timestamp != null) {
                val timeRightNow = Calendar.getInstance()
                val diff = timestamp.timeInMillis - timeRightNow.timeInMillis
                val diffHours = diff / (60 * 60 * 1000)
                diffHours
            } else {
                0
            }

            Log.d("WeatherViewModel", "Hours from timestamp: $hoursFromNow")
            Log.d("WeatherViewModel", "Timestamp: $timestamp")


            val weatherData = if(hoursFromNow < 3) {
                fetchNowCastData(route.latitude, route.longitude)
            } else {
                fetchLocationForecastData(route.latitude, route.longitude, timestamp)
            }

            weatherData
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Error: $e")
            null
        }
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
                val nowCastData = fetchNowCastData(latitude, longitude)
                _uiState.value = nowCastData
            } catch (e: Exception) {
                Log.e("NowCastViewModel", "Error: $e")
            }
        }
    }

    /**
     * Fetch the data from the API and update the UI.
     */

    suspend fun fetchNowCastData(
        latitude: Double,
        longitude: Double
    ): WeatherUiState = suspendCoroutine { continuation ->
        nowCastDataSource.getNowCastData(
            latitude,
            longitude,
            onSuccess = { response ->
                continuation.resume(
                    WeatherUiState(
                        isLoading = false,
                        symbolCode = response.properties.timeseries.first().data.next_1_hours.summary.symbol_code,
                        temperature = response.properties.timeseries.first().data.instant.details.air_temperature,
                        windSpeed = response.properties.timeseries.first().data.instant.details.wind_speed,
                        windDirection = response.properties.timeseries.first().data.instant.details.wind_from_direction,
                        updatedAt = response.properties.meta.updated_at
                    )
                )
            },
            onError = { error ->
                continuation.resumeWithException(Exception(error))
            }
        )
    }


    fun formatToISO8601(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

    private suspend fun fetchLocationForecastData(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar
    ): WeatherUiState = suspendCoroutine { continuation ->
        locationForecastDataSource.getLongTermWeatherData(
            latitude,
            longitude,
            onSuccess = { response ->
                // Remove minutes and seconds from the timestamp
                val zeroedTimestamp = timestamp.clone() as Calendar
                zeroedTimestamp.set(Calendar.DAY_OF_MONTH, timestamp.get(Calendar.DAY_OF_MONTH))
                zeroedTimestamp.set(Calendar.YEAR, timestamp.get(Calendar.YEAR))
                zeroedTimestamp.set(Calendar.HOUR_OF_DAY, timestamp.get(Calendar.HOUR_OF_DAY))
                zeroedTimestamp.set(Calendar.MINUTE, 0)
                zeroedTimestamp.set(Calendar.SECOND, 0)
                zeroedTimestamp.set(Calendar.MILLISECOND, 0)
                val timestampString = formatToISO8601(zeroedTimestamp)

                var data = response.properties.timeseries.find {
                    it.time == timestampString
                }
                if (data == null) {
                    // Find the closest value between 0, 6, 12, and 18 to the current hour
                    val currentHour = timestamp.get(Calendar.HOUR_OF_DAY)
                    val closestHour = listOf(0, 6, 12, 18).minByOrNull { abs(it - currentHour) }

                    // Loop through the hours starting from the closest hour
                    for (hour in closestHour!!..23) {
                        zeroedTimestamp.set(Calendar.HOUR_OF_DAY, hour)
                        val timestampString = formatToISO8601(zeroedTimestamp)
                        Log.d(
                            "WeatherViewModel",
                            "Looking for data for the timestamp: $timestampString"
                        )

                        // Replace the following with your actual data retrieval method
                        data = response.properties.timeseries.find {
                            it.time == timestampString
                        }
                        if (data != null) {
                            Log.d(
                                "WeatherViewModel",
                                "Found data for the timestamp: $timestampString"
                            )
                            break
                        }
                    }
                    Log.e("WeatherViewModel", "No data found for the timestamp: $timestampString")
                }


                Log.d("WeatherViewModel", "Lat/lon/timestamp: $latitude, $longitude, $timestampString")
                Log.d("WeatherViewModel", "Data: $data")

                continuation.resume(
                    WeatherUiState(
                        isLoading = false,
                        symbolCode = if (data?.data?.next_1_hours != null) {
                            data?.data?.next_1_hours?.summary?.symbol_code ?: "TEST"
                        } else {
                            data?.data?.next_6_hours?.summary?.symbol_code ?: "TEST"
                        },
                        temperature = data?.data?.instant?.details?.air_temperature ?: 0.0,
                        windSpeed = data?.data?.instant?.details?.wind_speed ?: 0.0,
                        windDirection = data?.data?.instant?.details?.wind_from_direction ?: 0.0,
                        updatedAt = response.properties.meta.updated_at ?: ""
                    )
                )
            },
            onError = { error ->
                continuation.resumeWithException(Exception(error))
            }
        )
    }



}