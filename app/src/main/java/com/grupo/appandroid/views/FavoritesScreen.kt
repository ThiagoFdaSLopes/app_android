package com.grupo.appandroid.views

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
    ) {
        Text(text = "Favorites Screen")
    }
}