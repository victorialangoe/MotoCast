package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.repository.MotoCastRepositoryInterface
import com.example.motocast.domain.utils.Utils.getCorrectAlertsFromAlerts
import com.example.motocast.ui.viewmodel.current_weather.RouteWeatherUiState
import java.util.*

class GetWeatherDataUseCase(
    private val repository: MotoCastRepositoryInterface,
    private val fetchLocationForecastDataUseCase: FetchLocationForecastDataUseCase,
    private val fetchNowCastDataUseCase: FetchNowCastDataUseCase,
) {
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
        Log.d("GetWeatherDataUseCase", "Weather data: $weatherData")
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
        else null
    }

    private fun calculateHoursFromNow(timestamp: Calendar?): Double {
        return timestamp?.let {
            val timeRightNow = Calendar.getInstance()
            val diff = timestamp.timeInMillis - timeRightNow.timeInMillis
            diff.toDouble() / (60 * 60 * 1000)
        } ?: 0.0
    }
}