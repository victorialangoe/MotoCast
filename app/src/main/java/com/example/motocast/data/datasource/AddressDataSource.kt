package com.example.motocast.data.datasource

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.data.api.address.AddressHelper
import com.example.motocast.data.model.AddressSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressDataSource : ViewModel() {
    private val addressHelper = AddressHelper().createAddressHelperDataAPI()

    fun getAddressData(
        query: String,
        onSuccess: (AddressSearchResult) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val response = addressHelper.searchAddress(query).execute()
                Log.d("AddressDataSource", "Success: ${response}")
                if (response.isSuccessful) {
                    val addressData = response.body()
                    if (addressData != null) {

                        onSuccess(addressData)
                    } else {
                        onError("Empty response")
                    }
                } else {
                    onError("Error: ${response.errorBody()}")
                }
            }
        }
    }

}

