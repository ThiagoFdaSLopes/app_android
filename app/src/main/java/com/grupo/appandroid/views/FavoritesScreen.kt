package com.grupo.appandroid.views

import JobFavoritesCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun FavoritesScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(WindowInsets.navigationBars.asPaddingValues()) // Adiciona espaço da barra de navegação
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.Start // Pode ajustar conforme o design
        ) {
            Text(
                text = "Vagas favoritadas",
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp) // Ajuste de espaço abaixo da foto
            )
            Spacer(modifier = Modifier.height(40.dp))
            JobFavoritesCard(onCardClick = {}, onHeartClick = {})
        }
        // NavigationBar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // Ajuste adicional para evitar colisões com a barra
        ) {
            NavigationBar(
                onSettingsClick = { navController.navigate("SettingsScreen") },
                onPeopleClick = { navController.navigate("PeopleScreen") },
                onBriefcaseClick = { navController.navigate("BriefcaseScreen") },
                onBellClick = { navController.navigate("NotificationsScreen") },
                onStarClick = { navController.navigate("FavoritesScreen") })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen(navController = NavController(LocalContext.current))
}