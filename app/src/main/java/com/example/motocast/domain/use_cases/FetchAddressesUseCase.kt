package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.repository.MotoCastRepositoryInterface
import com.example.motocast.domain.utils.Utils.filterSearchResults
import com.example.motocast.domain.utils.Utils.getAirDistanceFromLocation
import com.example.motocast.ui.viewmodel.address.Address

class FetchAddressesUseCase(
    private val motoCastRepositoryInterface: MotoCastRepositoryInterface,
    private val getLocationUseCase: GetLocationUseCase,
) {
    suspend operator fun invoke(query: String): List<Address> {
        if (query.isEmpty()) return emptyList()

        val response = motoCastRepositoryInterface.getAddresses(query) ?: run {
            Log.d("FetchAddressesUseCase", "invoke: null")
            return emptyList()
        }

        val userLocation = getLocationUseCase()

        val addresses = response.addresses.take(200).map { address ->

            val distanceFromUser = getAirDistanceFromLocation(
                address.representationPoint.latitude,
                address.representationPoint.longitude,
                userLocation
            )

            Address(
                addressText = address.addressText,
                municipality = address.municipalityName,
                latitude = address.representationPoint.latitude,
                longitude = address.representationPoint.longitude,
                distanceFromUser = distanceFromUser
            )
        }

        return filterSearchResults(query, addresses)
    }

}