package com.example.motocast.data.api.metalerts

import com.example.motocast.data.model.MetAlertsDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MetAlertsAPI {
    @GET("metalerts/1.1/.json")
    fun getMetAlertsData(
    ): Call<MetAlertsDataModel>
}