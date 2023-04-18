package com.example.motocast.ui.view.dynamic_scaffold.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.util.buttons.BasicButton
import com.example.motocast.util.buttons.FilledButton

@Composable
fun EditRouteButton(onNavigateToScreen: () -> Unit) {
    FilledButton(
        onClick = onNavigateToScreen,
        text = "Endre rute",
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    )
}