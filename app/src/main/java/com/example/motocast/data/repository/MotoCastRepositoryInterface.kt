package com.example.motocast.data.repository

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.content.Context
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel

interface MotoCastRepositoryInterface {
    suspend fun getAddresses(query: String): AddressDataModel?

    suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel?

    suspend fun getDirectionsData(coordinates: String): DirectionsDataModel?

    suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel?

    suspend fun getLocationsForecastData(latitude: Double, longitude: Double): LocationForecastDataModel?

    suspend fun getMetAlertsData(): MetAlertsDataModel?

    suspend fun getAppContext(): Context
}