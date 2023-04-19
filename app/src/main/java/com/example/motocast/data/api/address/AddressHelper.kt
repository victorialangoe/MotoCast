package com.example.motocast.data.api.address

import com.example.motocast.BuildConfig
import com.example.motocast.util.data.DataHelper

class AddressHelper: DataHelper() {

    fun createAddressHelperDataAPI(): AddressApi? {
        return createAPI(
            apiClass = AddressApi::class.java,
            baseUrl = BuildConfig.ADDRESS_API_BASE_URL
        )
    }
}