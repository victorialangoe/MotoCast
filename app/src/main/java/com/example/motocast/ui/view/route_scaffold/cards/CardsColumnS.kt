package com.example.motocast.ui.view.route_scaffold.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.view.route_scaffold.cards.CardS

@Composable
fun CardsColumnS () {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){

        item{
            CardS(12, "Gjerstad", 10, 0)
        }

        item{
            CardS(8, "Oslo", 12, 2)
        }

        item{
            CardS(-9, "Sandnessjøen", 9, 17)
        }

        item{
            CardS(12, "Gjerstad", 22, 0, true)
        }

        item{
            CardS(12, "Gjerstad", 10, 0)
        }

        item{
            CardS(12, "Gjerstad", 10, 0, true)
        }
    }
}