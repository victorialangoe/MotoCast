package com.example.motocast.data.remote.mock

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel
import com.example.motocast.data.remote.RemoteDataSourceInterface

class MockRemoteDataSource: RemoteDataSourceInterface {


    override suspend fun getAddresses(query: String): AddressDataModel? {
        return null
    }

    override suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel? {
        return null
    }

    override suspend fun getDirectionsData(coordinates: String): DirectionsDataModel? {
        return null
    }

    override suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel? {
        return null
    }

    override suspend fun getLocationsForecastData(latitude: Double, longitude: Double): LocationForecastDataModel? {
        return null
    }

    override suspend fun getMetAlertsData(): MetAlertsDataModel? {
        return null
    }

}