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

    /**
     * Fetches geocoded name from the repository, which is the human readable name of a coordinate.
     * It also removes ", Norge" from the name, since it is not needed.
     *
     * @param longitude The longitude of the coordinate, as a [Double]
     * @param latitude The latitude of the coordinate, as a [Double]
     *
     * @return geocoded name as a [String] or null
     */
    suspend operator fun invoke(longitude: Double, latitude: Double): String? {
        val response = repository.getReverseGeocoding(
            longitude = longitude,
            latitude = latitude
        ) ?: return null

        val name = response.features.firstOrNull()?.placeName

        return name?.replace(", Norge", "") ?: name
    }

}