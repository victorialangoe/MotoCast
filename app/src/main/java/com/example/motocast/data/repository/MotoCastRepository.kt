package com.example.motocast.data.repository

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.app.Application
import android.util.Log
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.AddressesApi
import com.example.motocast.data.api.DirectionsApi
import com.example.motocast.data.api.ReverseGeocodingApi
import com.example.motocast.data.api.LocationForecastApi
import com.example.motocast.data.api.MetAlertsApi
import com.example.motocast.data.api.NowCastApi
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.data.remote.RemoteDataSource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MotoCastRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val appContext: Application
) : MotoCastRepositoryInterface {

    override suspend fun getAddresses(query: String): AddressDataModel? {
        return remoteDataSource.getAddresses(query)
    }

    override suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel? {
        return remoteDataSource.getReverseGeocoding(latitude, longitude)
    }

    override suspend fun getDirectionsData(
        coordinates: String,
    ): DirectionsDataModel? {
        return remoteDataSource.getDirectionsData(coordinates)
    }

    override suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel? {
        return remoteDataSource.getNowCastData(latitude, longitude)
    }

    override suspend fun getLocationsForecastData(
        latitude: Double,
        longitude: Double,
    ): LocationForecastDataModel? {
        return remoteDataSource.getLocationsForecastData(latitude, longitude)
    }

    override suspend fun getMetAlertsData(): MetAlertsDataModel? {
        return remoteDataSource.getMetAlertsData()
    }

    override suspend fun getAppContext(): Application {
        return appContext
    }
}