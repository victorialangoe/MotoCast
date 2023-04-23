package com.example.motocast.data.api


import com.example.motocast.data.model.DirectionsDataModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call

interface DirectionsApi {
    @GET("directions/v5/mapbox/driving/{coordinates}")
    fun getDirections(
        @Path("coordinates") coordinates: String,
        @Query("alternatives") alternatives: Boolean = false,
        @Query("geometries") geometries: String = "geojson",
        @Query("language") language: String = "en",
        @Query("overview") overview: String = "simplified",
        @Query("steps") steps: Boolean = true,
        @Query("access_token") accessToken: String
    ): Call<DirectionsDataModel>


}