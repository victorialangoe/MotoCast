package com.example.motocast.data.repository

import AddressDataModel
import LocationForecastDataModel
import ReverseGeocodingDataModel
import android.app.Application
import android.util.Log
import com.example.motocast.BuildConfig
import com.example.motocast.data.api.address.AddressesApi
import com.example.motocast.data.api.geocoding.ReverseGeocodingApi
import com.example.motocast.data.api.location_forecast.LocationForecastApi
import com.example.motocast.data.api.metalerts.MetAlertsApi
import com.example.motocast.data.api.nowcast.NowCastApi
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.NowCastDataModel

import com.example.motocast.domain.repository.MotoCastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MotoCastRepositoryImp @Inject constructor(
    private val addressesApi: AddressesApi,
    private val nowCastApi: NowCastApi,
    private val reverseGeocodingApi: ReverseGeocodingApi,
    private val metAlertsApi: MetAlertsApi,
    private val locationForecastApi: LocationForecastApi,
    private val appContext: Application
) : MotoCastRepository {

    override suspend fun getAddresses(query: String): AddressDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<AddressDataModel> =
                    addressesApi.getAddresses(query).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
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

    override suspend fun getNowCastData(latitude: Double, longitude: Double): NowCastDataModel? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<NowCastDataModel> =
                    nowCastApi.getNowCastData(latitude, longitude).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
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
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getAppContext(): Application {
        return appContext
    }
}