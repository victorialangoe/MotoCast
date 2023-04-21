package com.example.motocast.data.datasource

import com.example.motocast.BuildConfig
import com.example.motocast.data.api.directions.DirectionsHelper
import com.example.motocast.data.model.RouteSearchResult
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DirectionsDataSource : DataHelper() {
    private val directionsRetrofitService = DirectionsHelper().createDirectionsAPI()

    suspend fun getDirectionsData(
        coordinates: String,
    ): RouteSearchResult? {
        return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = {
                    directionsRetrofitService?.getDirections(
                        coordinates = coordinates,
                        accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
                    )?.execute()
                },
                onSuccess = { directionsData: RouteSearchResult -> directionsData },
                onError = { Throwable("Error: ${it.message}") }
            )
        }
    }
}
