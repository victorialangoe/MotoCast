package com.example.motocast.data.datasource

import com.example.motocast.data.api.nowcast.NowCastHelper
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NowCastDataSource : DataHelper() {
    private val metRetrofitService = NowCastHelper().createNowCastAPI()
     suspend fun getNowCastData(
        latitude: Double,
        longitude: Double
    ): NowCastDataModel? {
         return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = { metRetrofitService?.getNowCastData(latitude, longitude)?.execute() },
                onSuccess = { nowCastData: NowCastDataModel -> nowCastData },
                onError = { Throwable("Error: ${it.message}") }
            )
        }
    }
}