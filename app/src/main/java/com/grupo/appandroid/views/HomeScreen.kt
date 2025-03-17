package com.grupo.appandroid.views

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.MenuIcon
import com.grupo.appandroid.components.ResumoConta
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite
import com.grupo.appandroid.viewmodels.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    isCompanyLogin: Boolean
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val email = prefs.getString("loggedInEmail", null)

    // Verifica se o email foi recuperado; caso contrário, exibe uma mensagem ou redireciona para o login
    if (email.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.message_email_not_found), color = TextWhite)
        }
        return
    }

    val userRepository = UserRepository(context)
    val companyRepository = CompanyRepository(context)
    // Define se o login é de usuário (quando não for login de empresa)
    val isUser = !isCompanyLogin
    // Recupera o nome com um fallback, evitando o uso do operador !!
    val name = if (isUser) {
        userRepository.findUserByEmail(email)?.name ?: stringResource(id = R.string.people)
    } else {
        companyRepository.findByEmail(email)?.companyName ?: stringResource(id = R.string.company)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Box para alinhar a foto de perfil e mensagem de boas-vindas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.hello) + "$name",
                    color = TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
            }
            // Menu com ícones
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
                    ) {
                        // Se for usuário, navega para PersonalProfileScreen; senão, para CompanyProfileScreen
                        navController.navigate(
                            if (isUser) "PersonalProfileScreen" else "CompanyProfileScreen"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                // Resumo da conta
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResumoConta()
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        // NavigationBar na parte inferior da tela
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {

            NavigationBar(navController = navController)
}
        }
}
