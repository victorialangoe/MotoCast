package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.inputs.InputViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
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