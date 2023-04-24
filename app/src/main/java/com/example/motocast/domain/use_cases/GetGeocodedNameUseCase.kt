package com.example.motocast.domain.use_cases

import com.example.motocast.data.repository.MotoCastRepository

class GetGeocodedNameUseCase(
    private val repository: MotoCastRepository
) {

    suspend operator fun invoke(longitude: Double, latitude: Double): String? {
        val response = repository.getReverseGeocoding(
            longitude = longitude,
            latitude = latitude
        ) ?: return null

        val name = response.features.firstOrNull()?.placeName

        return name?.replace(", Norge", "") ?: name
    }

}