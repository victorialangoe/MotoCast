package com.example.motocast.data.api

import LongTermWeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the Met API.
 * This interface is used to create a Retrofit instance.
 */
interface LongTermWeatherDataAPI {
    @GET("locationforecast/2.0/compact")
    fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<LongTermWeatherData>
}