package com.example.motocast.ui.view.dynamicScaffold.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun AddNewRouteButton(onNavigateToScreen: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onNavigateToScreen,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .defaultMinSize(minWidth = 200.dp, minHeight = 48.dp)
                .weight(0.8f)
        ) {
            Text(
                text = "Add new route",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_start_24),
            contentDescription = "Start new route icon",
            tint = Color.Black,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Preview
@Composable
fun AddNewRouteButtonPreview() {
    AddNewRouteButton() {}
}
