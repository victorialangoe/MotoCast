package com.example.motocast.ui.viewmodel.address

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.AddressDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddressDataViewModel : ViewModel() {
    private val addressDataSource = AddressDataSource()
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Fetches address data from the API and updates the UI state.
     * @param query The query to search for.
     * @return A list of addresses, limited to 50
     */
    fun fetchAddressData(query: String): List<Addresses> {
        if (query.length <= 6) {
            return emptyList()
        }

        _uiState.value = _uiState.value.copy(isLoading = true, query = query)

        addressDataSource.getAddressData(
            query = query,
            onSuccess = { addressSearchResult ->
                val addresses = addressSearchResult.adresser.take(50).map { address ->
                    Log.d("AddressDataViewModel", "Success: ${address.adressetekst}")
                    Addresses(
                        addressText = address.adressetekst,
                        municipality = address.kommunenavn
                    )
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    addresses = addresses
                )
            },
            onError = {
                Log.d("AddressDataViewModel", "Error: $it")
            }
        )

        return uiState.value.addresses.take(50)
    }
}


