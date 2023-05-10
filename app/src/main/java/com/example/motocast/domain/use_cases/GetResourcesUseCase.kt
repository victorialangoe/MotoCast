package com.example.motocast.domain.use_cases

import android.content.res.Resources
import com.example.motocast.data.repository.MotoCastRepository

class GetResourcesUseCase(
    private val repository: MotoCastRepository
) {
    operator fun invoke(): Resources {
        return repository.appContext.resources
    }
}
