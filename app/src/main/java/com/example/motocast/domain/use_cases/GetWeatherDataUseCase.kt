package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.domain.utils.Utils.getCorrectAlertsFromAlerts
import com.example.motocast.ui.viewmodel.current_weather.RouteWeatherUiState
import java.util.*

/**
 * Fetches weather data from the repository
 *
 * @param repository The repository to fetch the weather data from, as a [MotoCastRepository]
 * @param fetchLocationForecastDataUseCase The use case to fetch location forecast data, as a
 * [FetchLocationForecastDataUseCase]
 * @param fetchNowCastDataUseCase The use case to fetch nowcast data, as a [FetchNowCastDataUseCase]
 * @return weather data as a [RouteWeatherUiState] or null
 */
class GetWeatherDataUseCase(
    private val repository: MotoCastRepository,
    private val fetchLocationForecastDataUseCase: FetchLocationForecastDataUseCase,
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
) {

    /**
     * Fetches weather data from the repository, which can be either nowcast or location forecast.
     * Then it creates a [RouteWeatherUiState] object from the data and returns it.
     *
     * @param latitude The latitude to search for, as a [Double]
     * @param longitude The longitude to search for, as a [Double]
     * @param timestamp The timestamp to search for, as a [Calendar]
     *
     * @return weather data as a [RouteWeatherUiState] or null
     */
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar,
    ): RouteWeatherUiState? {

        val hoursFromNow = calculateHoursFromNow(timestamp)
        val alerts = repository.getMetAlertsData()

        val weatherData = if (hoursFromNow < 2) {
            fetchNowCastDataUseCase(latitude, longitude)
        } else {
            fetchLocationForecastDataUseCase(latitude, longitude, timestamp)
        }
        return if (weatherData != null) {
            RouteWeatherUiState(
                symbolCode = weatherData.symbolCode,
                temperature = weatherData.temperature,
                windSpeed = weatherData.windSpeed,
                windDirection = weatherData.windDirection,
                updatedAt = weatherData.updatedAt,
                alerts = getCorrectAlertsFromAlerts(latitude, longitude, timestamp, alerts)
            )
        }
        else {
            Log.d("GetWeatherDataUseCase", "invoke: null")
            null
        }
    }

    /** Helper function to calculate the hours from now to the given timestamp
     *
     * @param timestamp The timestamp to calculate the hours from now to, as a [Calendar]
     * @return The hours from now to the given timestamp, as a [Double]
     */
    private fun calculateHoursFromNow(timestamp: Calendar?): Double {
        return timestamp?.let {
            val timeRightNow = Calendar.getInstance()
            val diff = timestamp.timeInMillis - timeRightNow.timeInMillis
            diff.toDouble() / (60 * 60 * 1000)
        } ?: 0.0
    }
}