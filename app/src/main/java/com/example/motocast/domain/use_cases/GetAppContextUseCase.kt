package com.example.motocast.domain.use_cases

import android.content.Context
import com.example.motocast.data.repository.MotoCastRepository

/**
 * Fetches the application context from the repository and returns it.
 * @param repository The repository to fetch the application context from, as a [MotoCastRepository]
 * @return application context as a [Context]
 */
class GetAppContextUseCase(
    private val repository: MotoCastRepository
) {

    /**
     * Fetches the application context from the repository and returns it.
     * @return application context as a [Context]
     */
    suspend operator fun invoke(): Context {
        return repository.getAppContext()
    }
}