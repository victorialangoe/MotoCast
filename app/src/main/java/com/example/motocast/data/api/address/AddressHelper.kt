package com.example.motocast.data.api.address

import android.util.Log
import com.example.motocast.BuildConfig
import com.example.motocast.util.DataHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddressHelper: DataHelper() {

    fun createAddressHelperDataAPI(): AddressApi? {
        return createAPI(
            apiClass = AddressApi::class.java,
            baseUrl = BuildConfig.ADDRESS_API_BASE_URL
        )
    }
}