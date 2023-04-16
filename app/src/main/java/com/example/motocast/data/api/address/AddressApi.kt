package com.example.motocast.data.api.address

import AddressSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the kartverket API for address data.
 */
interface AddressApi {
    @GET("sok")
    fun searchAddress(
        @Query("sok") searchQuery: String,
        @Query("fuzzy") fuzzy: Boolean = true,
        @Query("sokemodus") searchMode: String = "OR",
        @Query("utkoordsys") coordinateSystem: Int = 4258,
        @Query("treffPerSide") resultsPerPage: Int = 200,
        @Query("side") page: Int = 0,
        @Query("asciiKompatibel") asciiCompatible: Boolean = true
    ): Call<AddressSearchResult>
}
