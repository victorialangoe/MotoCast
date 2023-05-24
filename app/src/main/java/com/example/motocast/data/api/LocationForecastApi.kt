package com.example.motocast.data.api

import LocationForecastDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the Met API for location forecast data.
 * Link to the API documentation: [Location Forecast API](https://api.met.no/weatherapi/locationforecast/2.0/documentation)
 */
interface LocationForecastApi {
    @GET("locationforecast/2.0/compact")
    fun getLocationForecastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<LocationForecastDataModel>
}