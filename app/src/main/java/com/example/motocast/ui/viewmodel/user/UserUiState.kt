package com.example.motocast.ui.viewmodel.user

data class UserUiState (
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLoading: Boolean = false,
    val updatedAt: String? = null,
    val error: String? = null,
)