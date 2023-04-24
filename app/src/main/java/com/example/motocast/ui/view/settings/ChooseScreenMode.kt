package com.example.motocast.ui.view.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.motocast.R

@Composable
fun ChooseScreenMode(
    darkMode: Boolean,
    setDarkMode: (Boolean) -> Unit
) {
    TextAndSwitch(
        text = stringResource(R.string.dark_mode),
        checked = darkMode,
        setDarkMode = setDarkMode
    )
}