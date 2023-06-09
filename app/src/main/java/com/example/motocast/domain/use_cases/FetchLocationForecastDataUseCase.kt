package com.example.motocast.domain.use_cases

import LocationForecastData
import LocationForecastDataModel
import android.util.Log
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.domain.utils.Utils.formatToISO8601
import com.example.motocast.domain.utils.Utils.getZeroedTimestamp
import com.example.motocast.ui.viewmodel.current_weather.WeatherUiState
import java.util.*
import kotlin.math.abs

/**
 * Fetches location forecast data from the repository
 * @param repository The repository to fetch the location forecast data from, as a [MotoCastRepository]
 * @return location forecast data as a [WeatherUiState] or null
 */
class FetchLocationForecastDataUseCase(
    private val repository: MotoCastRepository
) {

    /**
     * Fetches the closest weather data from the LocationForecastDataModel data
     * based on the timestamp.
     *
     * @param longitude The longitude of the location, as a [Double]
     * @param latitude The latitude of the location, as a [Double]
     * @param timestamp The timestamp to search for, as a [Calendar]
     * @return the closest weather data as a [WeatherUiState] or null
     */
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

    /**
     * Finds the closest weather data from the LocationForecastDataModel data
     * based on the timestamp.
     *
     * @param response The response from the repository, as a [LocationForecastDataModel]
     * @param zeroedTimestamp The timestamp to find the closest weather data for, as a [Calendar]
     */
    private fun findClosestWeatherData(
        response: LocationForecastDataModel,
        zeroedTimestamp: Calendar
    ): LocationForecastData? {
        var timeseries = response.properties.timeseries.find {
            it.time == formatToISO8601(zeroedTimestamp)
        }
        if (timeseries == null) {
            val currentHour = zeroedTimestamp.get(Calendar.HOUR_OF_DAY)
            val closestHour = listOf(0, 6, 12, 18).minByOrNull { abs(it - currentHour) }

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







