package com.example.motocast.data.datasource

import LongTermWeatherData
import com.example.motocast.data.api.location_forecast.LocationForecastHelper
import com.example.motocast.util.data.DataHelper


class LocationForecastDataSource: DataHelper() {
    private val metRetrofitService = LocationForecastHelper().createLongTermWeatherDataAPI()
    fun getLongTermWeatherData(
        latitude: Double,
        longitude: Double,
        onSuccess: (LongTermWeatherData) -> Unit,
        onError: (String) -> Unit
    ) {
        if (metRetrofitService != null) {
            fetchData(
                apiCall = { metRetrofitService.getWeatherData(latitude, longitude).execute() },
                onSuccess = { longTermWeatherData: LongTermWeatherData ->
                    onSuccess(
                        longTermWeatherData
                    )
                },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: metRetrofitService is null")
        }
    }

}

