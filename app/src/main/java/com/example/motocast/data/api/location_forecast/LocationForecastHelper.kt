package com.example.motocast.data.api.location_forecast


import LocationForecastDataModel
import com.example.motocast.BuildConfig
import com.example.motocast.util.data.DataHelper

class LocationForecastHelper: DataHelper() {

    fun createLongTermWeatherDataAPI(): LocationForecastDataModel? {
        return createAPI(
            apiClass = LocationForecastDataModel::class.java,
            baseUrl = BuildConfig.MET_API_BASE_URL,
            apiKey = BuildConfig.MET_API_KEY,
            headers = mapOf("X-Gravitee-API-Key" to BuildConfig.MET_API_KEY)
        )
    }
}