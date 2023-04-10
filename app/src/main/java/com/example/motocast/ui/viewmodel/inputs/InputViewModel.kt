package com.example.motocast.ui.viewmodel.inputs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InputViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InputUiState())
    val uiState: StateFlow<InputUiState> = _uiState.asStateFlow()



    fun addDestination(destination: String) {

    }

    fun removeFields() {
        _uiState.value = _uiState.value.copy(numberOfFields = 0)
        Log.d("Remove textfields", _uiState.value.numberOfFields.toString())

    }

    fun addField() {

        _uiState.value = _uiState.value.copy(numberOfFields = _uiState.value.numberOfFields + 1)
        Log.d("Add textfield", _uiState.value.numberOfFields.toString())


    }

}