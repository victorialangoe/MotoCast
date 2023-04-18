package com.example.motocast.data.api.geocoding

import com.example.motocast.BuildConfig
import com.example.motocast.util.DataHelper

class ReverseGeocodingHelper: DataHelper() {
    fun createReverseGeocodingAPI(): ReverseGeocodingApi? {
        return createAPI(
        apiClass = ReverseGeocodingApi::class.java,
        baseUrl = BuildConfig.MAPBOX_API_BASE_URL
    )
}

}