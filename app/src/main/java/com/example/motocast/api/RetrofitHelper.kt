package com.example.motocast.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    val baseUrl = "https://api.openrouteservice.org/"

    fun getDirectionApi(): DirectionApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionApi::class.java)
    }
}