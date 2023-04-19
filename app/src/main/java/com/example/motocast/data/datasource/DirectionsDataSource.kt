package com.example.motocast.data.datasource

import com.example.motocast.BuildConfig
import com.example.motocast.data.api.directions.DirectionsHelper
import com.example.motocast.data.model.RouteSearchResult
import com.example.motocast.util.data.DataHelper

class DirectionsDataSource : DataHelper() {
    private val directionsRetrofitService = DirectionsHelper().createDirectionsAPI()

    fun getDirectionsData(
        coordinates: String,
        onSuccess: (RouteSearchResult) -> Unit,
        onError: (String) -> Unit
    ) {
        if (directionsRetrofitService != null) {
            fetchData(
                apiCall = { directionsRetrofitService.getDirections(coordinates = coordinates, accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN).execute()},
                onSuccess = { directionsData: RouteSearchResult -> onSuccess(directionsData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: directionsRetrofitService is null")
        }
    }

}