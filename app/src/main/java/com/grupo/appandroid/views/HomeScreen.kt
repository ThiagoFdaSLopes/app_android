package com.grupo.appandroid.views

import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite
import com.grupo.appandroid.viewmodels.LoginViewModel

@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val email = prefs.getString("loggedInEmail", null)
    val userRepository = UserRepository(context)

    val user = userRepository.findUserByEmail(email = email.toString())
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
                    .fillMaxWidth().height(100.dp)
            ) {
                // Texto de boas-vindas
                Text(
                    text = "Olá, ${user?.name}",
                    color = TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp) // Ajuste de espaço abaixo da foto
                )
            }
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
                    ) { navController.navigate("VagasScreen") }
                    MenuIcon(
                        icon = R.drawable.heart,
                        label = ""
                    ) { navController.navigate("FavoritesScreen") }
                    MenuIcon(
                        icon = R.drawable.icon_people,
                        label = ""
                    ) { navController.navigate("PersonalProfileScreen") }
                }
                Spacer(modifier = Modifier.height(1.dp)) // Espaço entre as linhas de ícones
                // Resumo da conta (Descrições)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResumoConta()

                }
                Spacer(modifier = Modifier.height(8.dp))
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
