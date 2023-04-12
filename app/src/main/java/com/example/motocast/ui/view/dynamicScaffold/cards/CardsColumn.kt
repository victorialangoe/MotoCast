package com.example.motocast.ui.view.dynamicScaffold.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CardsColumn () {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){

        item{
            Card(12, "Gjerstad", 10, 0)
        }

        item{
            Card(8, "Oslo", 12, 2)
        }

        item{
            Card(-9, "Sandnessjøen", 9, 17)
        }

        item{
            Card(12, "Gjerstad", 22, 0, true)
        }

        item{
            Card(12, "Gjerstad", 10, 0)
        }

        item{
            Card(12, "Gjerstad", 10, 0, true)
        }
    }
}