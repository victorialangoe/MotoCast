package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.domain.repository.MotoCastRepository
import com.example.motocast.ui.viewmodel.weather.WeatherUiState

class FetchNowCastDataUseCase(
    private val motoCastRepository: MotoCastRepository
) {

    suspend operator fun invoke(latitude: Double, longitude: Double): WeatherUiState? {
        val response = motoCastRepository.getNowCastData(latitude, longitude) ?: run {
            Log.d("FetchNowCastDataUseCase", "invoke: null")
            return null
        }
        val firstTimeseries = response.properties.timeseries.first().data

        return WeatherUiState(
            symbolCode = firstTimeseries.next_1_hours.summary.symbol_code,
            temperature = firstTimeseries.instant.details.air_temperature,
            windSpeed = firstTimeseries.instant.details.wind_speed,
            windDirection = firstTimeseries.instant.details.wind_from_direction,
            updatedAt = response.properties.meta.updated_at
        )
    }
}
