package com.example.motocast

import retrofit2.Retrofit


class GetRoutingAPI {
    private val BASEURL = "https://api.openrouteservice.org/v2/"
    private val apiKey = "" // Empty to avoid scraping
    private val retrofit = Retrofit.Builder().baseUrl(BASEURL).build()



    fun getRoutingAPI() {
    }
    // TODO: Implement the ViewModel

    }