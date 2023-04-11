package com.example.motocast.ui.view.dynamicScaffold.badges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R

@Composable
fun DateBadge() {
    Row(modifier = Modifier
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = Color(0xfff7f7f7))
        .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.calendar),
            contentDescription = "Calendar icon",
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = "21.03")
    }
}