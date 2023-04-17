package com.example.motocast.data.api.directions


import com.example.motocast.data.datasource.DirectionsDataSource
import com.example.motocast.data.model.RouteSearchResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call

interface DirectionsApi {
    @GET("v5/mapbox/driving/{coordinates}")
    fun getDirections(
        @Path("coordinates") coordinates: String,
        @Query("alternatives") alternatives: Boolean = false,
        @Query("geometries") geometries: String = "geojson",
        @Query("language") language: String = "en",
        @Query("overview") overview: String = "simplified",
        @Query("steps") steps: Boolean = true,
        @Query("access_token") accessToken: String
    ): Call<RouteSearchResult>

}