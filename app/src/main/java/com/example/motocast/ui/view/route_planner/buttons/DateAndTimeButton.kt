package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.motocast.theme.Orange300
import com.example.motocast.theme.Orange500
import com.example.motocast.theme.Orange500Transparent

@Composable
fun DateAndTimeButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit? = {},
    icon: Int,
    iconDescription: String,
    enabled: Boolean = true,
) {
    Button(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange500Transparent,
            contentColor = Orange500,
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(1.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = iconDescription,
                modifier = Modifier.size(16.dp),
                tint = Orange300,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = label,
                color = Orange500,
            )
        }
    }
}