package com.example.motocast.domain.repository

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.content.Context
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel
import java.util.Calendar

interface MotoCastRepository {
    suspend fun getAddresses(query: String): AddressDataModel?

    suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel?

    suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel?

    suspend fun getLocationsForecastData(latitude: Double, longitude: Double): LocationForecastDataModel?

    suspend fun getMetAlertsData(): MetAlertsDataModel?

    suspend fun getAppContext(): Context
}