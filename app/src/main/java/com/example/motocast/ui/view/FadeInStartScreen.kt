package com.example.motocast.ui.view

import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun rememberAnimationState(): MutableState<Boolean> {
    val isAnimationComplete = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(2500) // our opening screen animation is 2500 milli sec
        isAnimationComplete.value = true
    }
    return isAnimationComplete
}
