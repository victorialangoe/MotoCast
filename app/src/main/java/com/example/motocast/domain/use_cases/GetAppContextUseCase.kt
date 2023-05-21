package com.example.motocast.domain.use_cases

import android.content.Context
import com.example.motocast.data.repository.MotoCastRepository

class GetAppContextUseCase(
    private val repository: MotoCastRepository
) {
    suspend operator fun invoke(): Context {
        return repository.getAppContext()
    }
}