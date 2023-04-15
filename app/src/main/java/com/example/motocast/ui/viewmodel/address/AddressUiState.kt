package com.example.motocast.ui.viewmodel.address

import android.location.Location
import com.example.motocast.ui.viewmodel.route_planner.Destination

data class AddressUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val addresses: List<Address> = emptyList(),
    // Hardcoded for now
    val formerAddresses: List<Address> = listOf(
        Address("Karl Johans gate 1", "Oslo", 59.9139, 10.7522, 0),
        Address("Trimveien 8", "Oslo", 59.94567031291559, 10.72444922406699, 0),
        Address("Holterveien 21", "Oslo", 59.6067418234209, 11.173867819648482, 0),
        Address("Nedre Ullevål 18", "Oslo", 59.940287213702085, 10.735926469761425, 0)
    ),
)
data class Address(
    val addressText: String,
    val municipality: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distanceFromUser: Int? = null
)
