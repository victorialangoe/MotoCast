package com.example.motocast.data.datasource

import LongTermWeatherData
import com.example.motocast.data.api.location_forecast.LocationForecastHelper
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocationForecastDataSource : DataHelper() {
    private val metRetrofitService = LocationForecastHelper().createLongTermWeatherDataAPI()

    suspend fun getLongTermWeatherData(
        latitude: Double,
        longitude: Double,
    ): LongTermWeatherData? {
        return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = {
                    metRetrofitService?.getWeatherData(latitude, longitude)?.execute()
                },
                onSuccess = { longTermWeatherData: LongTermWeatherData -> longTermWeatherData },
                onError = { Throwable("Error: ${it.message}") }
            )

        }
    }
}