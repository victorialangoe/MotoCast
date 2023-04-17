package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.util.buttons.BasicButton
import com.example.motocast.util.buttons.OutlinedButton

@Composable
fun ClearAllButton(
    clearAll: () -> Unit,
) {
    OutlinedButton(
        text = "Start på nytt",
        onClick = { clearAll() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent,
        ),
    )
}
