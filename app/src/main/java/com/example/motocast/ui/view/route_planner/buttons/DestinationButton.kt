package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DestinationButton(
    header: String,
    text: String,
    description: String,
    icon: Int,
    removeable: Boolean,
    removeFromRoute: () -> Unit,
    editAddress : () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = header,
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.weight(0.12f, fill = true)
            )
            Row(
                modifier = Modifier
                    .border(
                        0.5.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp)
                    .weight(0.88f, fill = true)
                    .clickable(onClick = editAddress),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = description
                )
                // Distance between icon and text
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 20.sp,
                )
            }
            if (removeable) {
                TextButton(onClick = removeFromRoute) {
                    Text(text = "Fjern")
                }
            }
        }
    }
}
