package com.example.motocast.data.datasource;

import ReverseGeocodingResult
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.geocoding.ReverseGeocodingHelper
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReverseGeocodingSource: DataHelper() {
    private val reverseGeocodingRetrofitService = ReverseGeocodingHelper().createReverseGeocodingAPI()

    suspend fun getReverseGeocodingData(
        longitude: Double,
        latitude: Double
    ): ReverseGeocodingResult? {
        return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = {
                    reverseGeocodingRetrofitService?.getReverseGeocode(longitude, latitude, BuildConfig.MAPBOX_ACCESS_TOKEN )
                        ?.execute()
                },
                onSuccess = { reverseGeocodingData: ReverseGeocodingResult -> reverseGeocodingData },
                onError = { Throwable("Error: ${it.message}") }
            )
        }
    }
}
