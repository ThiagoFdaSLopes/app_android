package com.grupo.appandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.Dicas
import com.grupo.appandroid.components.MenuIcon
import com.grupo.appandroid.components.ResumoConta
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(WindowInsets.navigationBars.asPaddingValues()) // Adiciona espaço da barra de navegação
    ) {
        // Organizando tudo dentro de uma única Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.Start // Pode ajustar conforme o design
        ) {
            // Box para alinhar a foto de perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fotoperfil),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .align(Alignment.TopEnd),
                    contentScale = ContentScale.Crop // Garante que a imagem se ajuste corretamente ao formato
                )


                // Texto de boas-vindas
                Text(
                    text = "Olá,\nCandidato",
                    color = TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp) // Ajuste de espaço abaixo da foto
                )
            }
            Spacer(modifier = Modifier.height(7.dp)) // Espaço entre texto e ícones


            // Menu com ícones (organizado em Column e Rows)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MenuIcon(
                        icon = R.drawable.icon_suitcases,
                        label = ""
                    ) { navController.navigate("candidateDetail/{name}/{age}/{location}/{role}/{experience}") }
                    MenuIcon(
                        icon = R.drawable.heart,
                        label = ""
                    ) { navController.navigate("FavoritesScreen") }
                    MenuIcon(
                        icon = R.drawable.icon_bell,
                        label = ""
                    ) { navController.navigate("NotificationsScreen") }
                }


                Spacer(modifier = Modifier.height(1.dp)) // Espaço entre as linhas de ícones


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MenuIcon(
                        icon = R.drawable.icon_upload,
                        label = ""
                    ) { navController.navigate("UploadScreen") }
                    MenuIcon(
                        icon = R.drawable.icon_people,
                        label = ""
                    ) { navController.navigate("UsersScreen") }
                    MenuIcon(
                        icon = R.drawable.icon_message,
                        label = ""
                    ) { navController.navigate("MessagesScreen") }
                }
                Spacer(modifier = Modifier.height(5.dp)) // Espaço entre as ícones e resumo da conta


                // Resumo da conta (Descrições)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResumoConta()

                }
                Spacer(modifier = Modifier.height(8.dp))
                // Dicas para o usuário
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    Dicas(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))


            }
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


//@Preview(showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
//
//}
