package com.example.motocast.data.api.directions

import com.example.motocast.BuildConfig
import com.example.motocast.util.DataHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit


class DirectionsHelper: DataHelper() {
    fun createDirectionsAPI(): DirectionsApi? {
        return createAPI(
            apiClass = DirectionsApi::class.java,
            baseUrl = BuildConfig.DIRECTIONS_API_BASE_URL
        )
    }
}
