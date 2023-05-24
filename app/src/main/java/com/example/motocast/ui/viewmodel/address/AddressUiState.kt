package com.example.motocast.ui.viewmodel.address

data class AddressUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val addresses: List<Address> = emptyList(),
    val formerAddresses: List<Address> = emptyList(),
)
data class Address(
    val addressText: String,
    val municipality: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distanceFromUser: Int? = null
)
