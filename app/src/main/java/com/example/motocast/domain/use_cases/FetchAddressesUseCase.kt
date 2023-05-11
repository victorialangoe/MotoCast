package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.domain.utils.Utils.filterSearchResults
import com.example.motocast.domain.utils.Utils.getAirDistanceFromLocation
import com.example.motocast.ui.viewmodel.address.Address

/**
 * Fetches addresses from the [repository] and returns them as a list of [Address].
 *
 * @param repository The repository to fetch the addresses from, as a [MotoCastRepository]
 * @param locationUseCase The use case to get the current location, as a [LocationUseCase]
 * @return addresses as a list of [Address] or empty list
 */
class FetchAddressesUseCase(
    private val repository: MotoCastRepository,
    private val locationUseCase: LocationUseCase
) {
    suspend operator fun invoke(query: String): List<Address> {
        if (query.isEmpty()) return emptyList()

        val response = repository.getAddresses(query) ?: run {
            Log.d("FetchAddressesUseCase", "invoke: null")
            return emptyList()
        }

        val userLocation = locationUseCase.getCurrentLocation()

        val addresses = response.addresses.take(200).map { address ->

            val distanceFromUser = if (userLocation != null) {
                 getAirDistanceFromLocation(
                    address.representationPoint.latitude,
                    address.representationPoint.longitude,
                    userLocation
                ) } else { null }


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