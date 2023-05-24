package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.repository.MotoCastRepository

/**
 * Fetches directions data from the repository and returns it as a [DirectionsDataModel].
 *
 * @param repository The repository to fetch the directions data from, as a [MotoCastRepository]
 * @return directions data as a [DirectionsDataModel] or null
 */
class FetchDirectionsDataUseCase(
    private val repository: MotoCastRepository
)
{

    /**
     * Fetches directions data from the repository and returns it as a [DirectionsDataModel].
     * @param coordinates The coordinates to search for, as a [String]
     * @return directions data as a [DirectionsDataModel] or null
     */
    suspend operator fun invoke(coordinates: String): DirectionsDataModel? {
        val response = repository.getDirectionsData(coordinates) ?: run {
            Log.d("FetchDirectionsDataUseCase", "invoke: null")
            return null
        }
        return response
    }
}
