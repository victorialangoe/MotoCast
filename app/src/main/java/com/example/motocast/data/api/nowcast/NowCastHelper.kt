package com.example.motocast.data.api.nowcast

import com.example.motocast.BuildConfig
import com.example.motocast.util.data.DataHelper

class NowCastHelper: DataHelper() {
    fun createNowCastAPI(): NowCastAPI? {
        return createAPI(
            apiClass = NowCastAPI::class.java,
            baseUrl = BuildConfig.MET_API_BASE_URL,
            apiKey = BuildConfig.MET_API_KEY,
            headers = mapOf("X-Gravitee-API-Key" to BuildConfig.MET_API_KEY)
        )
    }
}
