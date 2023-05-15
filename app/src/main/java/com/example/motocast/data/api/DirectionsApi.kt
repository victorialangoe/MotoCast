package com.example.motocast.data.api


import com.example.motocast.data.model.DirectionsDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface for the Mapbox API for directions data.
 * Link to the API documentation: [Mapbox Directions API](https://docs.mapbox.com/api/navigation/#directions)
 */
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