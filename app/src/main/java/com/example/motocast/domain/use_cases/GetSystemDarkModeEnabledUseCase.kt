package com.example.motocast.domain.use_cases

import android.content.res.Configuration
import com.example.motocast.data.repository.MotoCastRepository

/**
 * Fetches system dark mode enabled from the repository
 *
 * @param repository The repository to fetch the system dark mode enabled from
 * @return system dark mode enabled as a [Boolean]
 */
class GetSystemDarkModeEnabledUseCase(
    private val repository: MotoCastRepository
) {
    suspend operator fun invoke(): Boolean {
        val appContext = repository.getAppContext()
        return when (appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}