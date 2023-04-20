package com.example.motocast.data.datasource

import com.example.motocast.data.api.nowcast.NowCastHelper
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.util.data.DataHelper

class NowCastDataSource : DataHelper() {
    private val metRetrofitService = NowCastHelper().createNowCastAPI()
    fun getNowCastData(
        latitude: Double,
        longitude: Double,
        onSuccess: (NowCastDataModel) -> Unit,
        onError: (String) -> Unit
    ) {
        if (metRetrofitService != null) {
           return fetchData(
                apiCall = { metRetrofitService.getNowCastData(latitude, longitude).execute() },
                onSuccess = { nowCastData: NowCastDataModel -> onSuccess(nowCastData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: metRetrofitService is null")
        }
    }
}