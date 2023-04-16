package com.example.motocast.data.datasource

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.api.nowcast.NowCastHelper
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.util.DataHelper

class NowCastDataSource : DataHelper() {
    private val metRetrofitService = NowCastHelper().createNowCastAPI()
    fun getNowCastData(
        latitude: Double,
        longitude: Double,
        onSuccess: (NowCastDataModel) -> Unit,
        onError: (String) -> Unit
    ) {
        if (metRetrofitService != null) {
            Log.d("NowCastDataSource", "metRetrofitService is not null")
            fetchData(
                apiCall = { metRetrofitService.getNowCastData(latitude, longitude).execute() },
                onSuccess = { nowCastData: NowCastDataModel -> onSuccess(nowCastData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: metRetrofitService is null")
        }
    }
}