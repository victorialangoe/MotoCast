package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.ui.viewmodel.current_weather.WeatherUiState

/**
 * Fetches nowCast data from the repository
 *
 * @param repository The repository to fetch the nowcast data from, as a [MotoCastRepository]
 * @return nowCast data as a [WeatherUiState] or null
 */
class FetchNowCastDataUseCase(
    private val repository: MotoCastRepository
) {

    /**
     * Fetches nowCast data from the repository and returns it as a [WeatherUiState].
     *
     * @param latitude The latitude to search for, as a [Double]
     * @param longitude The longitude to search for, as a [Double]
     * @return nowCast data as a [WeatherUiState] or null
     */
    suspend operator fun invoke(latitude: Double, longitude: Double): WeatherUiState? {
        val response = repository.getNowCastData(latitude, longitude) ?: run {
            Log.d("FetchNowCastDataUseCase", "invoke: null")
            return null
        }
        val firstTimeSeries = response.properties.timeseries.first().data

        return WeatherUiState(
            symbolCode = firstTimeSeries.next_1_hours.summary.symbol_code,
            temperature = firstTimeSeries.instant.details.air_temperature,
            windSpeed = firstTimeSeries.instant.details.wind_speed,
            windDirection = firstTimeSeries.instant.details.wind_from_direction,
            updatedAt = response.properties.meta.updated_at
        )
    }
}
