package com.example.motocast.ui.viewmodel.address

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.AddressDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3

class AddressDataViewModel : ViewModel() {
    internal var addressDataSource = AddressDataSource()
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Fetches address data from the API and updates the UI state.
     * @param query The query to search for.
     * @return A list of addresses, limited to 50
     */

    fun addFormerAddress(address: Address) {

        val currentUiState = _uiState.value
        val newDestinations = currentUiState.formerAddresses.toMutableList()
        newDestinations.add(address)
        _uiState.value = currentUiState.copy(formerAddresses = newDestinations)
    }

    fun clearQuery() {
        _uiState.value = _uiState.value.copy(query = "")
    }

    fun fetchAddressData(
        query: String,
        getAirDistanceFromLocation: (Location) -> Int?,

    ): List<Address> {
        if (query.isEmpty()) {
            return emptyList()
        }

        _uiState.value = _uiState.value.copy(isLoading = true, query = query)


        addressDataSource.getAddressData(
            query = query,
            onSuccess = { addressSearchResult ->
                val addresses = addressSearchResult.addresses.take(200).map { address ->

                    val distanceFromUser = getAirDistanceFromLocation(
                        Location("").apply {
                            latitude = address.representationPoint.latitude
                            longitude = address.representationPoint.longitude
                        }
                    )

                    Address(
                        addressText = address.addressText,
                        municipality = address.municipalityName,
                        latitude = address.representationPoint.latitude,
                        longitude = address.representationPoint.longitude,
                        distanceFromUser = distanceFromUser
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

        return _uiState.value.addresses
    }

    fun clearResults() {
        _uiState.value = _uiState.value.copy(addresses = emptyList())
    }
    //for testing purposes
    fun getQuery(): String {
        return _uiState.value.query
    }
}


