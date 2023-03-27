package com.example.motocast.data.api.location_forecast

import com.example.motocast.BuildConfig
import com.example.motocast.data.api.LongTermWeatherDataAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MetRetrofitHelper {

    fun createLongTermWeatherDataAPI(): LongTermWeatherDataAPI {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .header("X-Gravitee-API-Key", BuildConfig.MET_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MET_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(LongTermWeatherDataAPI::class.java)
    }
}