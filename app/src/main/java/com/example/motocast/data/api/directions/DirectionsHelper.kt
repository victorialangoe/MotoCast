package com.example.motocast.data.api.directions

import okhttp3.OkHttpClient
import retrofit2.Retrofit


class DirectionsHelper {

    private val BASE_URL = "https://api.mapbox.com/directions/"

    fun createDirectionsAPI(): DirectionsApi {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .build()

        return retrofit.create(DirectionsApi::class.java)
    }
}