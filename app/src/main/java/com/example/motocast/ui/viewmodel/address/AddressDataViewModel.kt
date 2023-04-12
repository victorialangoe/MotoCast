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
    private val addressDataSource = AddressDataSource()
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Fetches address data from the API and updates the UI state.
     * @param query The query to search for.
     * @return A list of addresses, limited to 50
     */

    private fun updateCurrentUserLocation(
        getCurrentLocation: KFunction2<(location: Location) -> Unit, (exception: Exception) -> Unit, Unit>
    )
    {
        getCurrentLocation(
            { location ->
                _uiState.value = _uiState.value.copy(currentUserLocation = location)
            }, {
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

    fun clearQuery() {
        _uiState.value = _uiState.value.copy(query = "")
    }

    fun fetchAddressData(
        query: String,
        getCurrentLocation: KFunction2<(location: Location) -> Unit, (exception: Exception) -> Unit, Unit>,
        getAirDistanceFromPosToPos: KFunction3<Double, Double, Location, Int>

    ): List<Address> {
        if (query.length < 0) {
            return emptyList()
        }

        updateCurrentUserLocation(getCurrentLocation)

        _uiState.value = _uiState.value.copy(isLoading = true, query = query)


        addressDataSource.getAddressData(
            query = query,
            onSuccess = { addressSearchResult ->
                val addresses = addressSearchResult.adresser.take(200).map { address ->
                    Log.d("AddressDataViewModel", "Success: ${address.adressetekst}")

                    var distanceFromUser = if (_uiState.value.currentUserLocation != null) {
                        getAirDistanceFromPosToPos(
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
                        distanceFromUser = distanceFromUser?.toInt() ?: null
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


