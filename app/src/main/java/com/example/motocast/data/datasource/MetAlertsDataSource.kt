package com.example.motocast.data.datasource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.metalerts.MetAlertsHelper
import com.example.motocast.data.model.MetAlertsDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MetAlertsDataSource: ViewModel() {
    private val metRetrofitService = MetAlertsHelper().createMetAlertsAPI()

    fun getMetAlertsData(
        onSuccess: (MetAlertsDataModel) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = metRetrofitService.getMetAlertsData().execute()
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