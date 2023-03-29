package com.example.motocast.data.api.nowcast

import com.example.motocast.data.model.NowCastDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NowCastAPI {
    @GET("nowcast/2.0/complete")
    fun getNowCastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<NowCastDataModel>
}