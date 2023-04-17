package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.motocast.ui.view.dynamic_scaffold.buttons.AddNewRouteButton

@Composable
fun HomeScaffoldContent(
    onButtonClick : () -> Unit,
) {

        AddNewRouteButton(onButtonClick)
}
