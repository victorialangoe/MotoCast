package com.example.motocast.ui.view.dynamic_scaffold.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.util.buttons.FilledButton

@Composable
fun AddNewRouteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilledButton(
        onClick = onClick,
        text = "Legg til ny rute",
        modifier = modifier
    )
}
