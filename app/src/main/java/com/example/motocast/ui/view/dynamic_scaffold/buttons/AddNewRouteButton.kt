package com.example.motocast.ui.view.dynamic_scaffold.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.util.buttons.BasicButton
import com.example.motocast.util.buttons.FilledButton

@Composable
fun AddNewRouteButton(onClick: () -> Unit) {
    FilledButton(
        onClick = onClick,
        text = "Legg til ny rute",
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    )
}
