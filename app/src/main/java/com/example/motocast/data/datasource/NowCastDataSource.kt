package com.example.motocast.data.datasource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.nowcast.NowCastHelper
import com.example.motocast.data.model.NowCastDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NowCastDataSource : ViewModel() {
    private val metRetrofitService = NowCastHelper().createNowCastAPI()

    fun getNowCastData(
        latitude: Double,
        longitude: Double,
        onSuccess: (NowCastDataModel) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = metRetrofitService.getNowCastData(latitude, longitude).execute()
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        onSuccess(weatherData)
                    } else {
                        onError("Empty response")

                    }
                } else {
                    onError("Error: ${response.errorBody()}")
                }
            }
        }
    }
}