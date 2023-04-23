package com.example.motocast.data.api

import ReverseGeocodingDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReverseGeocodingApi {
    @GET("geocoding/v5/mapbox.places/{longitude},{latitude}.json")
    fun getReverseGeoCoding(
        @Path("longitude") longitude: Double,
        @Path("latitude") latitude: Double,
        @Query("access_token") accessToken: String,
        @Query("country") country: String? = null,
        @Query("language") language: String = "nb",
        @Query("limit") limit: Int = 1,
        @Query("reverseMode") reverseMode: String? = null,
        @Query("routing") routing: Boolean? = null,
        @Query("types") types: String = "place",
        @Query("worldview") worldview: String? = null
    ): Call<ReverseGeocodingDataModel>
}