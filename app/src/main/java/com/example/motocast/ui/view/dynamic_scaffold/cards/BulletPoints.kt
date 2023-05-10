package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BulletPoints(text: String) {
    val sentences = text.split(". ")
    Column {
        sentences.forEach { sentence ->
            if (sentence.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("•")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
