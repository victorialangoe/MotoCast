package com.example.motocast.ui.view.dynamic_scaffold.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
                    Text(sentence)
                }
            }
        }
    }
}
