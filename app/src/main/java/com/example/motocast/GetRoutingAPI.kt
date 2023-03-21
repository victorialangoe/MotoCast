package com.example.motocast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetRoutingAPI {
    private val BASE_URL = "https://api.openrouteservice.org/v2/"
    private val apiKey = "5b3ce3597851110001cf624844b3031074b6423697be6ece55076f23" // Empty to avoid scraping
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL).build()



    fun getRoutingAPI() {
    }


    }