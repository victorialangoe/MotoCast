package com.example.motocast.domain.use_cases

import android.content.res.Resources
import com.example.motocast.data.repository.MotoCastRepository

/**
 * Fetches the application resources from the repository and returns it.
 * @param repository The repository to fetch the application resources from, as a [MotoCastRepository]
 */
class GetResourcesUseCase(
    private val repository: MotoCastRepository
) {
    /**
     * Fetches the application resources from the repository and returns it.
     *
     * @return application resources as a [Resources]
     */
    operator fun invoke(): Resources {
        return repository.appContext.resources
    }
}
