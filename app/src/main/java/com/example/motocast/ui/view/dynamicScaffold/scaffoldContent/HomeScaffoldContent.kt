package com.example.motocast.ui.view.dynamicScaffold.scaffoldContent

import androidx.compose.runtime.Composable
import com.example.motocast.ui.view.home_bottom_scaffold.AddNewRouteButton
import com.example.motocast.ui.view.home_bottom_scaffold.favorites.FavoritesSection

@Composable
fun HomeScaffoldContent(
    onNavigateToScreen: () -> Unit
) {

    AddNewRouteButton(onNavigateToScreen)

    FavoritesSection()
}