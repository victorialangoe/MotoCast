package com.example.motocast.data.datasource

import AddressSearchResult
import com.example.motocast.data.api.address.AddressHelper
import com.example.motocast.util.data.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddressDataSource: DataHelper() {
    private val addressRetrofitService = AddressHelper().createAddressHelperDataAPI()

    suspend fun getAddressData(
        query: String,
    ): AddressSearchResult? {
        return withContext(Dispatchers.IO) {
            fetchData(
                apiCall = { addressRetrofitService?.searchAddress(query)?.execute() },
                onSuccess = { addressData: AddressSearchResult -> addressData },
                onError = { Throwable("Error: ${it.message}") }
            )
        }
    }
}


