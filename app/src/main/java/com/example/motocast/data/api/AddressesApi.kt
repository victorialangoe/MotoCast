package com.example.motocast.data.api

import com.example.motocast.data.model.AddressDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the kartverket API for address data.
 * Link to the API documentation: [Kartverket API](https://ws.geonorge.no/adresser/v1/)
 */
interface AddressesApi {
    @GET("sok")
    fun getAddresses(
        @Query("sok") searchQuery: String,
        @Query("fuzzy") fuzzy: Boolean = true,
        @Query("sokemodus") searchMode: String = "OR",
        @Query("utkoordsys") coordinateSystem: Int = 4258,
        @Query("treffPerSide") resultsPerPage: Int = 500,
        @Query("side") page: Int = 0,
        @Query("asciiKompatibel") asciiCompatible: Boolean = true
    ): Call<AddressDataModel>
}
