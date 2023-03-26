package com.example.motocast.data.datasource

import LongTermWeatherData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.MetRetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WeatherDataSource : ViewModel() {
    private val metRetrofitService = MetRetrofitHelper().createLongTermWeatherDataAPI()

    fun getLongTermWeatherData(
        latitude: Double,
        longitude: Double,
        onSuccess: (LongTermWeatherData) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = metRetrofitService.getWeatherData(latitude, longitude).execute()
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

