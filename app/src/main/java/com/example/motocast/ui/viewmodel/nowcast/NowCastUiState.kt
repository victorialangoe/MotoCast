package com.example.motocast.ui.viewmodel.nowcast

import com.example.motocast.data.model.NowCastDataModel

data class NowCastUiState (
    val isLoading: Boolean = false,
    val weatherData: NowCastDataModel? = null,
    val error: String? = null,
    val updatedAt: String? = null,
)
