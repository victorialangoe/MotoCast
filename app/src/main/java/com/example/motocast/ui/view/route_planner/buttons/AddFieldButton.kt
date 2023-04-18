package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.util.buttons.BasicButton
import com.example.motocast.util.buttons.FilledButton

@Composable
fun AddFieldButton(
    addDestination: () -> Unit,
    enabled: Boolean = true,
) {
    FilledButton(
        enabled = enabled,
        text = if (enabled) "Legg til stopp " else "Maks 10 stopp",
        onClick = addDestination,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}