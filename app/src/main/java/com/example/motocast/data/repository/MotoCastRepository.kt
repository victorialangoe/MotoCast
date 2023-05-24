package com.example.motocast.data.repository

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.app.Application
import android.content.Context
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.data.remote.RemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * A repository class that implements [MotoCastRepositoryInterface] to manage data retrieval
 * from the [RemoteDataSource] and provide the application context.
 *
 * @param remoteDataSource The remote data source for retrieving weather, geocoding, and other data.
 * @param appContext The application context for context-related operations.
 * @see MotoCastRepositoryInterface
 */

class MotoCastRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @ApplicationContext val appContext: Context,

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
        return appContext as Application
    }

}