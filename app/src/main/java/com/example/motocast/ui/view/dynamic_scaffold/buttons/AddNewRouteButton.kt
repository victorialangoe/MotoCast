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

@Composable
fun AddNewRouteButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceTint)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 55.dp),
            text = "Legg til ny rute",
            onClick = onClick,
        )
    }
}

@Preview
@Composable
fun AddNewRouteButtonPreview() {
    AddNewRouteButton() {}
}
