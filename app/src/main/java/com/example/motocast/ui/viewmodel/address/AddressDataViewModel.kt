package com.example.motocast.ui.viewmodel.address

import androidx.lifecycle.ViewModel
import com.example.motocast.domain.use_cases.FetchAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddressDataViewModel @Inject constructor(
    private val fetchAddressesUseCase: FetchAddressesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun fetchAddressData(query: String): List<Address> {
        updateUiState { it.copy(isLoading = true, query = query) }

        val addresses = fetchAddressesUseCase(query)

        updateUiState { it.copy(addresses = addresses, isLoading = false) }

        return addresses
    }
    private fun updateUiState(update: (AddressUiState) -> AddressUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun addFormerAddress(address: Address) {
        updateUiState { it.copy(formerAddresses = it.formerAddresses + address) }
    }

    fun clearQuery() = updateUiState { it.copy(query = "") }

    fun setQuery(query: String) = updateUiState { it.copy(query = query) }

    fun clearResults() = updateUiState { it.copy(addresses = emptyList()) }

    fun getQuery(): String = _uiState.value.query
}

