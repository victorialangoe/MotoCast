package com.example.motocast.ui.view.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.theme.Orange500

@Composable
fun TextAndSwitch(
    text: String,
    checked: Boolean,
    setScreenMode: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.weight(1f)) // Spacer to push the switch to the right
        Switch(
            enabled = !checked,
            checked = checked,
            onCheckedChange = { setScreenMode() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Orange500,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
            ),
            thumbContent = {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected screen mode icon",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        )
    }
}