package com.example.motocast.ui.view.dynamicScaffold.scaffoldContent

import androidx.compose.runtime.Composable
import com.example.motocast.ui.view.dynamicScaffold.buttons.AddNewRouteButton

@Composable
fun HomeScaffoldContent(
    onNavigateToScreen: () -> Unit
) {

    AddNewRouteButton(onNavigateToScreen)

    // FavoritesSection()
}