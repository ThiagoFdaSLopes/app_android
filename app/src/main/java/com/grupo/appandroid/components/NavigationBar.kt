package com.grupo.appandroid.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.utils.SessionManager
import com.grupo.appandroid.viewmodels.LoginViewModel

@Composable
fun NavigationBar(
    navController: NavController,
) {
    val context = LocalContext.current
    val loginViewModel = LoginViewModel()
    val sessionManager = SessionManager(context)
    val isCompanyLogin = sessionManager.isCompanyLogin()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable {   navController.navigate(
                    if (!isCompanyLogin) "PersonalProfileScreen" else "CompanyProfileScreen"
                ) }
        )
        Icon(
            painter = painterResource(id = R.drawable.briefcase),
            contentDescription = "People",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable { if(isCompanyLogin) {
                        navController.navigate("CandidatesScreen")
                    } else {
                        navController.navigate("VagasScreen")
                    }
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Briefcase",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(52.dp)
                .clickable {  navController.navigate("home") }
        )
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "Star",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable {  navController.navigate("FavoritesScreen")}
        )
        Icon(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Loggout",
            tint = Color.White,
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .clickable {  loginViewModel.logout(context)
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    } }
        )

    }
}
