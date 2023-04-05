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
        onSuccess: (MetAlertsDataModel, Map<String, String>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = metRetrofitService.getMetAlertsData().execute()
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        val weatherIconMap = mutableMapOf<String, String>()
                        for (feature in weatherData.features) {
                            val properties = feature.properties
                            val event = properties.event
                            val awarenessLevel = properties.awareness_level
                            val icon = getWeatherIcon(event, awarenessLevel)
                            weatherIconMap[event] = icon
                        }
                        onSuccess(weatherData, weatherIconMap)
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