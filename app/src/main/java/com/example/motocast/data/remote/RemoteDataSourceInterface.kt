package com.example.motocast.data.remote

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel

/**
 * Interface for the remote data source.
 * Contains functions for getting data from the remote data source.
 * @see RemoteDataSource
 */
interface RemoteDataSourceInterface {
    suspend fun getAddresses(query: String): AddressDataModel?

    suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel?

    suspend fun getDirectionsData(coordinates: String): DirectionsDataModel?

    suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel?

    suspend fun getLocationsForecastData(latitude: Double, longitude: Double): LocationForecastDataModel?

    suspend fun getMetAlertsData(): MetAlertsDataModel?

}