package com.example.motocast.data.api

import com.example.motocast.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper class for creating a Retrofit instance for the Met API.
 * This makes handling the JSON data easier.
 * @see [Retrofit]
 * @see [GsonConverterFactory]
 * @see [OkHttpClient]
 * @see [LongTermWeatherDataAPI]
 */
class MetRetrofitHelper {


    /**
     * Creates a Retrofit instance for the Met API.
     * This instance is used to create a [LongTermWeatherDataAPI] instance.
     * @see [LongTermWeatherDataAPI]
     */
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