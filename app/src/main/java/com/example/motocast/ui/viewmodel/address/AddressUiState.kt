package com.example.motocast.ui.viewmodel.address
data class AddressUiState(
    val isLoading: Boolean = false,
    val query: String? = null,
    val addresses: List<Address> = emptyList(),
    val error: String? = null
)

data class Address(
    val addressText: String,
    val municipality: String?,
    val latitude: Double?,
    val longitude: Double?,
)
