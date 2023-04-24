package com.example.motocast.data.remote

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.util.Log
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.*
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RemoteDataSource(
    private val reverseGeocodingApi: ReverseGeocodingApi,
    private val addressesApi: AddressesApi,
    private val directionsApi: DirectionsApi,
    private val locationForecastApi: LocationForecastApi,
    private val metAlertsApi: MetAlertsApi,
    private val nowCastApi: NowCastApi,
): RemoteDataSourceInterface {



    override suspend fun getAddresses(query: String): AddressDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<AddressDataModel> =
                    addressesApi.getAddresses(query).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getAddresses: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                Log.d("MotoCastRepositoryImp", "getAddresses: ${e.message}")
                null
            }
        }
    }

    override suspend fun getReverseGeocoding(latitude: Double, longitude: Double): ReverseGeocodingDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<ReverseGeocodingDataModel> =
                    reverseGeocodingApi.getReverseGeoCoding(
                        latitude = latitude,
                        longitude = longitude,
                        accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getReverseGeocoding: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                Log.d("MotoCastRepositoryImp", "getReverseGeocoding: ${e.message}")
                null
            }
        }
    }

    override suspend fun getDirectionsData(
        coordinates: String,
    ): DirectionsDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<DirectionsDataModel> =
                    directionsApi.getDirections(
                        coordinates = coordinates,
                        accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN).execute()
                Log.d("MotoCastRepositoryImp", "getDirectionsData: ${response}")
                if (response.isSuccessful) {
                    Log.d("MotoCastRepositoryImp", "getDirectionsData: ${response.body()}")
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getDirectionsData: ${response}")
                    null
                }
            } catch (e: Exception) {
                Log.d("MotoCastRepositoryImp", "getDirectionsData: ${e.message}")
                null
            }
        }
    }

    override suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<NowCastDataModel> =
                    nowCastApi.getNowCastData(latitude, longitude).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getNowCastData: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                Log.d("MotoCastRepositoryImp", "getNowCastData: ${e.message}")
                null
            }
        }
    }

    override suspend fun getLocationsForecastData(
        latitude: Double,
        longitude: Double,
    ): LocationForecastDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<LocationForecastDataModel> =
                    locationForecastApi.getLocationForecastData(
                        latitude,
                        longitude,
                    ).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getLocationsForecastData: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getMetAlertsData(): MetAlertsDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<MetAlertsDataModel> =
                    metAlertsApi.getMetAlertsData().execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.d("MotoCastRepositoryImp", "getMetAlertsData: ${response.errorBody()}")
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

}