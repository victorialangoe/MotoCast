package com.example.motocast.ui.view.settings

import androidx.compose.runtime.Composable

@Composable
fun ChooseScreenMode(
    darkMode: Boolean,
    setDarkMode: (Boolean) -> Unit
) {
    TextAndSwitch(
        text = "Mørk modus",
        checked = darkMode,
        setDarkMode = setDarkMode
    )
}