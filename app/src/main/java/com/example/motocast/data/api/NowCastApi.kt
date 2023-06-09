package com.example.motocast.data.api

import com.example.motocast.data.model.NowCastDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the Met API for nowcast data.
 * Link to the API documentation: [Nowcast API](https://api.met.no/weatherapi/nowcast/2.0/documentation)
 */
interface NowCastApi {
    @GET("nowcast/2.0/complete")
    fun getNowCastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<NowCastDataModel>

}