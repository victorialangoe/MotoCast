package com.example.motocast.domain.use_cases

import com.example.motocast.data.repository.MotoCastRepository

/**
 * Fetches geocoded name from the repository
 *
 * @param repository The repository to fetch the geocoded name from, as a [MotoCastRepository]
 * @return geocoded name as a [String] or null
 */
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