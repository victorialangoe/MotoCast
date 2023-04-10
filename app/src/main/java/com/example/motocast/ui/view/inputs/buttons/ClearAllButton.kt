package com.example.motocast.ui.view.inputs.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.inputs.InputViewModel

@Composable
fun ClearAllButton(inputViewModel: InputViewModel) {
    OutlinedButton(
        onClick = {inputViewModel.removeFields()},
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .width(width = 340.dp)
            .height(height = 55.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Start på nytt",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
    }
}