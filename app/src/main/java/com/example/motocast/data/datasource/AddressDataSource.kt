package com.example.motocast.data.datasource

import AddressSearchResult
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.api.address.AddressHelper
import com.example.motocast.util.DataHelper

class AddressDataSource: DataHelper() {
    private val addressRetrofitService = AddressHelper().createAddressHelperDataAPI()

    fun getAddressData(
        query: String,
        onSuccess: (AddressSearchResult) -> Unit,
        onError: (String) -> Unit
    ) {
        if (addressRetrofitService != null) {
            fetchData(
                apiCall = { addressRetrofitService.searchAddress(query).execute() },
                onSuccess = { addressData: AddressSearchResult -> onSuccess(addressData) },
                onError = { errorMessage: String -> onError(errorMessage) }
            )
        } else {
            onError("Error: addressRetrofitService is null")
        }
    }
}

