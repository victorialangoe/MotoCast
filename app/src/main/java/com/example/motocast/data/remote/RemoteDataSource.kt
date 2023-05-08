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
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Class that handles the remote data source.
 * @param reverseGeocodingApi The API interface for reverse geocoding data.
 * @param addressesApi The API interface for addresses data.
 * @param directionsApi The API interface for directions data.
 * @param locationForecastApi The API interface for location forecast data.
 * @param metAlertsApi The API interface for metalerts data.
 * @param nowCastApi The API interface for nowcast data.
 * @see RemoteDataSourceInterface
 */
class RemoteDataSource(
    private val reverseGeocodingApi: ReverseGeocodingApi,
    private val addressesApi: AddressesApi,
    private val directionsApi: DirectionsApi,
    private val locationForecastApi: LocationForecastApi,
    private val metAlertsApi: MetAlertsApi,
    private val nowCastApi: NowCastApi,
): RemoteDataSourceInterface {


    /**
     * Function that gets the addresses data from the met API.
     * @param query The query to search for.
     * @return The addresses data as a [AddressDataModel] object.
     */
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

    /**
     * Function that gets the reverse geocoding data from the mapbox API.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The reverse geocoding data as a [ReverseGeocodingDataModel] object.
     */
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

    /**
     * Function that gets the directions data from the mapbox API.
     * @param coordinates The coordinates of the location as a string.
     * @return The directions data as a [DirectionsDataModel] object.
     */
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
                    val errorBody = response.errorBody()?.string()
                    val errorModel = Gson().fromJson(errorBody, DirectionsDataModel::class.java)
                    Log.d("MotoCastRepositoryImp", "getDirectionsData: Error: $errorModel")
                    errorModel
                }
            } catch (e: Exception) {
                Log.d("MotoCastRepositoryImp", "getDirectionsData: ${e.message}")
                null
            }
        }
    }


    /**
     * Function that gets the nowcast data from the met API.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The nowcast data as a [NowCastDataModel] object.
     */
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

    /**
     * Function that gets the location forecast data from the met API.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The location forecast data as a [LocationForecastDataModel] object.
     */
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

    /**
     * Function that gets the metalerts data from the met API.
     * @return The metalerts data as a [MetAlertsDataModel] object.
     */
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