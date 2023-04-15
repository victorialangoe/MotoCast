package com.example.motocast.util.badges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun BasicBadge(
    leadingIcon: Int,
    text: String,
    ){

    Row(modifier = Modifier
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = Color(0xfff7f7f7))
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            imageVector = ImageVector.vectorResource(id = leadingIcon),
            contentDescription = "Calendar icon",
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = text)
    }
}