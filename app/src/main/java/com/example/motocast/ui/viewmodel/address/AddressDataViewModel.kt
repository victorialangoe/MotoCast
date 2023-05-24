package com.example.motocast.ui.viewmodel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motocast.domain.use_cases.FetchAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressDataViewModel @Inject constructor(
    private val fetchAddressesUseCase: FetchAddressesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    private var searchJob: Job? = null

    val uiState = _uiState.asStateFlow()

    suspend fun fetchAddressData(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, query = query) }

            delay(300)

            val addresses = fetchAddressesUseCase(query)

            updateUiState { it.copy(addresses = addresses, isLoading = false) }
        }
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

}

