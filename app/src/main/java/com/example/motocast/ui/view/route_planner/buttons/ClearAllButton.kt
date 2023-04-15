package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.util.buttons.BasicButton

@Composable
fun ClearAllButton(
    clearAll: () -> Unit,
) {
    BasicButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 55.dp),
        text = "Start på nytt",
        outlined = true,
        onClick = {
            clearAll()
        },
    )
}