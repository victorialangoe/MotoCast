package com.example.motocast.data.api.location_forecast


import com.example.motocast.BuildConfig
import com.example.motocast.data.api.LongTermWeatherDataAPI
import com.example.motocast.util.data.DataHelper

class LocationForecastHelper: DataHelper() {

    fun createLongTermWeatherDataAPI(): LongTermWeatherDataAPI? {
        return createAPI(
            apiClass = LongTermWeatherDataAPI::class.java,
            baseUrl = BuildConfig.MET_API_BASE_URL,
            apiKey = BuildConfig.MET_API_KEY,
            headers = mapOf("X-Gravitee-API-Key" to BuildConfig.MET_API_KEY)
        )
    }
}