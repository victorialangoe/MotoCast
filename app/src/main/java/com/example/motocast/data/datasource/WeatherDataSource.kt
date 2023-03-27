package com.example.motocast.data.datasource

import LongTermWeatherData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.MetRetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This class is responsible for fetching data from the API and returning it to the ViewModel.
 * The ViewModel will then pass the data to the UI. This class is a singleton, which means that
 * only one instance of this class can exist at a time. This is done to prevent multiple instances
 * of this class from being created, which would cause unnecessary network calls.
 */
class WeatherDataSource : ViewModel() {
    private val metRetrofitService = MetRetrofitHelper().createLongTermWeatherDataAPI()

    /**
     * This function is responsible for fetching data from the LocationForecastAPI.
     * It uses the latitude and longitude to get the weather data for that location.
     *
     * The data is returned as a [LongTermWeatherData] object.
     * If the data is successfully fetched, the [onSuccess] function is called.
     * If the data is not successfully fetched, the [onError] function is called.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param onSuccess The function to be called if the data is successfully fetched.
     * @param onError The function to be called if the data is not successfully fetched.
     */
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

