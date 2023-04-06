package com.example.motocast.ui.viewmodel.address
data class AddressUiState(
    val isLoading: Boolean = false,
    val query: String? = null,
    val addresses: List<Addresses> = emptyList(),
    val error: String? = null
)

data class Addresses(
    val addressText: String,
    val municipality: String,
)
