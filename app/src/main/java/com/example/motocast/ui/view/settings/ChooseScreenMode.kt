package com.example.motocast.ui.view.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.motocast.ui.viewmodel.settings.ScreenMode

@Composable
fun ChooseScreenMode(
    setScreenMode: (ScreenMode) -> Unit,
    screenMode: ScreenMode
) {

    Column {

        TextAndSwitch(
            text = "Mørk modus",
            checked = screenMode == ScreenMode.DARK,
            setScreenMode = { setScreenMode(ScreenMode.DARK) }
        )

        TextAndSwitch(
            text = "Lys modus",
            checked = screenMode == ScreenMode.LIGHT,
            setScreenMode = { setScreenMode(ScreenMode.LIGHT) }
        )

        TextAndSwitch(
            text = "System modus",
            checked = screenMode == ScreenMode.PREFER_SYSTEM,
            setScreenMode = { setScreenMode(ScreenMode.PREFER_SYSTEM) }
        )

    }
}
