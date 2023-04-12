package com.example.motocast.ui.viewmodel.address

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.AddressDataSource
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.route_planner.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class AddressDataViewModel : ViewModel() {
    private val addressDataSource = AddressDataSource()
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Fetches address data from the API and updates the UI state.
     * @param query The query to search for.
     * @return A list of addresses, limited to 50
     */

    private fun updateCurrentUserLocation(mapLocationViewModel: MapLocationViewModel) {
        mapLocationViewModel.getCurrentLocation(
            onSuccess = { location ->
                _uiState.value = _uiState.value.copy(currentUserLocation = location)
            },
            onError = {
                Log.d("AddressDataViewModel", "Error: $it")
            }
        )
    }

    fun addFormerAddress(address: Address) {

        val currentUiState = _uiState.value
        val newDestinations = currentUiState.formerAddresses.toMutableList()
        newDestinations.add(address)
        _uiState.value = currentUiState.copy(formerAddresses = newDestinations)
        Log.d("AddressDataViewModel", "Added former address: ${address.addressText}")
    }

    fun fetchAddressData(query: String, mapLocationViewModel: MapLocationViewModel): List<Address> {
        if (query.length < 0) {
            return emptyList()
        }

        updateCurrentUserLocation(mapLocationViewModel)

        _uiState.value = _uiState.value.copy(isLoading = true, query = query)


        addressDataSource.getAddressData(
            query = query,
            onSuccess = { addressSearchResult ->
                val addresses = addressSearchResult.adresser.take(200).map { address ->
                    Log.d("AddressDataViewModel", "Success: ${address.adressetekst}")

                    var distanceFromUser = if (_uiState.value.currentUserLocation != null) {
                        mapLocationViewModel.getAirDistanceFromPosToPos(
                            address.representasjonspunkt.lat,
                            address.representasjonspunkt.lon,
                            _uiState.value.currentUserLocation!!
                        )
                    } else {
                        null
                    }

                    Address(
                        addressText = address.adressetekst,
                        municipality = address.kommunenavn,
                        latitude = address.representasjonspunkt.lat,
                        longitude = address.representasjonspunkt.lon,
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
}


