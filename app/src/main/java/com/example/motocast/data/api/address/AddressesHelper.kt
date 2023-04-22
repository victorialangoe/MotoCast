package com.example.motocast.data.api.address

import com.example.motocast.BuildConfig
import com.example.motocast.util.data.DataHelper

class AddressesHelper: DataHelper() {

    fun createAddressHelperDataAPI(): AddressesApi? {
        return createAPI(
            apiClass = AddressesApi::class.java,
            baseUrl = BuildConfig.ADDRESS_API_BASE_URL
        )
    }
}