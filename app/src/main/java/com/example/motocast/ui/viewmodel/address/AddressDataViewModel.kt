package com.example.motocast.ui.viewmodel.address

import AddressSearchResult
import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.motocast.data.datasource.AddressDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

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

    fun setQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    private fun cleanAndNormalize(str: String): String {
        return str.lowercase()
            .replace("å", "a")
            .replace("ø", "o")
            .replace("æ", "ae")
    }
    fun searchResultsCompareBy(query: String): Comparator<Address> {
        return compareBy<Address> {
            if (cleanAndNormalize(it.addressText) == cleanAndNormalize(query)) 0 else 1
        }.thenBy {
            if (cleanAndNormalize(it.municipality ?: "") == cleanAndNormalize(query)) 0 else 1
        }.thenBy {
            it.distanceFromUser
        }
    }


    suspend fun fetchAddressData(
        query: String,
        getAirDistanceFromLocation: (Location) -> Int?,

        ): List<Address> {
        if (query.isEmpty()) {
            return emptyList()
        }

        _uiState.value = _uiState.value.copy(isLoading = true, query = query)

        val response: AddressSearchResult? = addressDataSource.getAddressData(query)

        if (response != null) {
            val addresses = response.addresses.take(200).map { address ->

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
            _uiState.value = _uiState.value.copy(addresses = addresses)
            // wait for 500 second before setting isLoading to false to avoid flickering
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }, 200)
        } else {
            throw Exception("Response is null")
        }

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


