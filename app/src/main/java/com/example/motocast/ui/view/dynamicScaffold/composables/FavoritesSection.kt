package com.example.motocast.ui.view.dynamicScaffold.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesSection() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        item {
            FavoritesCard("Favorite 1", Icons.Filled.Favorite)
        }
        item {
            FavoritesCard("Favorite 2", Icons.Filled.Favorite)
        }
        item {
            FavoritesCard("Favorite 3", Icons.Filled.Favorite)
        }
        item {
            FavoritesCard("Favorite 4", Icons.Filled.Favorite)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesCard(title: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = Color.White)
            .padding(8.dp)
            // max size of the card
            .size(240.dp, 80.dp),
    ) {
        Column {
            Text (
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text (
                text = "Subtitle",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )

        }
        Row{
            Icon(
                imageVector = icon,
                contentDescription = "Favorite icon",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun FavoritesSectionPreview() {
    FavoritesSection()
}