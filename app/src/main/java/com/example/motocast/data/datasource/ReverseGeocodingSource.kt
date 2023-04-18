package com.example.motocast.data.datasource;

import ReverseGeocodingResult
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.geocoding.ReverseGeocodingHelper
import com.example.motocast.util.DataHelper

class ReverseGeocodingSource: DataHelper() {
    private val reverseGeocodingRetrofitService = ReverseGeocodingHelper().createReverseGeocodingAPI()

    fun getReverseGeocodingData(
        longitude: Double,
        latitude: Double,
        onSuccess: (ReverseGeocodingResult) -> Unit,
        onError: (String) -> Unit
    ) {
        if (reverseGeocodingRetrofitService != null) {
            fetchData(
                apiCall = { reverseGeocodingRetrofitService.getReverseGeocode(longitude, latitude, BuildConfig.MAPBOX_ACCESS_TOKEN ).execute() },
                onSuccess = { reverseGeocodingData: ReverseGeocodingResult -> onSuccess(reverseGeocodingData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: reverseGeocodingRetrofitService is null")
        }
    }

}
