package com.example.motocast.data.api

import com.example.motocast.data.model.MetAlertsDataModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface for the Met API for metalerts data.
 * Link to the API documentation: [Metalerts API](https://api.met.no/weatherapi/metalerts/1.1/documentation)
 */
interface MetAlertsApi {
    @GET("metalerts/1.1/.json")
    fun getMetAlertsData(
    ): Call<MetAlertsDataModel>
}