package com.example.motocast.ui.view.route_planner.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
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
                tint = MaterialTheme.colorScheme.onTertiary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = label,
                color = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}