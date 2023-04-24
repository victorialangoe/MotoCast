package com.example.motocast.domain.use_cases

import android.content.res.Configuration
import com.example.motocast.data.repository.MotoCastRepositoryInterface

class GetSystemDarkModeEnabledUseCase(
    private val repository: MotoCastRepositoryInterface
) {
    suspend operator fun invoke(): Boolean {
        val appContext = repository.getAppContext()
        return when (appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}