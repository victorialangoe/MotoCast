package com.example.motocast.data.api

import com.example.motocast.data.model.MetAlertsDataModel
import retrofit2.Call
import retrofit2.http.GET

interface MetAlertsApi {
    @GET("metalerts/1.1/.json")
    fun getMetAlertsData(
    ): Call<MetAlertsDataModel>
}