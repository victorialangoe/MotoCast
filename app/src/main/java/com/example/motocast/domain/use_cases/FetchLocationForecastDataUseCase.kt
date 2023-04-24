package com.example.motocast.domain.use_cases

import LocationForecastData
import LocationForecastDataModel
import android.util.Log
import com.example.motocast.data.repository.MotoCastRepositoryInterface
import com.example.motocast.domain.utils.Utils.formatToISO8601
import com.example.motocast.domain.utils.Utils.getZeroedTimestamp
import com.example.motocast.ui.viewmodel.current_weather.WeatherUiState
import java.util.*

class FetchLocationForecastDataUseCase(
    private val repository: MotoCastRepositoryInterface
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar
    ): WeatherUiState? {
        val response = repository.getLocationsForecastData(latitude, longitude) ?: run {
            Log.d("FetchLocationForecastDataUseCase", "invoke: null")
            return null
        }

        val zeroedTimestamp = getZeroedTimestamp(timestamp)
        val closestWeatherData = findClosestWeatherData(response, zeroedTimestamp)

        return closestWeatherData?.let {
            WeatherUiState(
                symbolCode = if (closestWeatherData.next_1_hours != null) {
                    closestWeatherData.next_1_hours.summary.symbol_code
                } else {
                    closestWeatherData.next_6_hours.summary.symbol_code
                },
                temperature = it.instant.details.air_temperature,
                windSpeed = it.instant.details.wind_speed,
                windDirection = it.instant.details.wind_from_direction,
                updatedAt = response.properties.meta.updated_at,
            )
        }

    }

    private fun findClosestWeatherData(
        response: LocationForecastDataModel,
        zeroedTimestamp: Calendar
    ): LocationForecastData? {
        var timeseries = response.properties.timeseries.find {
            it.time == formatToISO8601(zeroedTimestamp)
        }
        if (timeseries == null) {
            val currentHour = zeroedTimestamp.get(Calendar.HOUR_OF_DAY)
            val closestHour = listOf(0, 6, 12, 18).minByOrNull { Math.abs(it - currentHour) }

            for (hour in closestHour!!..23) {
                zeroedTimestamp.set(Calendar.HOUR_OF_DAY, hour)
                val timestampString = formatToISO8601(zeroedTimestamp)

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
        }
        return timeseries?.data
    }

}







