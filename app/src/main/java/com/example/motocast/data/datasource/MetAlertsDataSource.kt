package com.example.motocast.data.datasource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.metalerts.MetAlertsHelper
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.ui.view.getWeatherIcon
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
                    val weatherIconMap = mutableMapOf<String, String>()
                    if (weatherData != null) {
                        for (feature in weatherData.features) {
                            val properties = feature.properties
                            val event = properties.event
                            val awarenessLevel = properties.awareness_level
                            val icon = getWeatherIcon(event, awarenessLevel)
                            weatherIconMap[event] = icon
                            // data and icon created
                        }
                    }
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