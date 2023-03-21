package com.example.motocast.api

import com.example.motocast.navigation.FeatureCollection
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionApi {
    @GET("v2/directions/driving-car")
    suspend fun getDirection(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): FeatureCollection
}
