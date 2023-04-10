package com.example.motocast.data.api.address

import com.example.motocast.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddressHelper {

    fun createAddressHelperDataAPI(): AddressApi {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.ADDRESS_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AddressApi::class.java)
    }
}
