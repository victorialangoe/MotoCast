package com.example.motocast.domain.use_cases

import android.util.Log
import com.example.motocast.data.model.DirectionsDataModel
import com.example.motocast.data.repository.MotoCastRepository
import com.example.motocast.data.repository.MotoCastRepositoryInterface

class FetchDirectionsDataUseCase(
    private val repository: MotoCastRepository
)
{
    suspend operator fun invoke(coordinates: String): DirectionsDataModel? {
        Log.d("FetchDirectionsDataUseCase", "invoke: $coordinates")
        val response = repository.getDirectionsData(coordinates) ?: run {
            return null
        }
        return response
    }
}
