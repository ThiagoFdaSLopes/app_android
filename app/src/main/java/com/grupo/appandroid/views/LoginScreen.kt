package com.grupo.appandroid.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.ui.theme.*
import com.grupo.appandroid.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val userRepository = UserRepository(context)
    val companyRepository = CompanyRepository(context)
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    // Observe states
    val email = loginViewModel.email.observeAsState("")
    val password = loginViewModel.password.observeAsState("")
    val isLoading = loginViewModel.isLoading.observeAsState(initial = false)
    val errorMessage = loginViewModel.errorMessage.observeAsState("")
    val loginSuccess = loginViewModel.loginSuccess.observeAsState()

    // Handle login success
    loginSuccess.value?.let { result ->
        LaunchedEffect (result) {
            prefs.edit()
                .putString("loggedInEmail", result.email)
                .putString("loginType", result.type)
                .apply()
            navController.navigate("home")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = stringResource(id = R.string.welcome_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(
                text = stringResource(id = R.string.welcome_back),
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            CustomTextField(
                label = stringResource(id = R.string.email),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                value = email.value,
                onValueChange = { loginViewModel.updateEmail(it) }
            )

            CustomTextField(
                label = stringResource(id = R.string.password),
                isPassword = true,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
                value = password.value,
                onValueChange = { loginViewModel.updatePassword(it) }
            )

            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = TextGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.forget_password),
                color = TextGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Start
            )

            Button(
                onClick = {
                    loginViewModel.login(userRepository, companyRepository)
                },
                enabled = !isLoading.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberPrimary,
                    contentColor = TextWhite
                )
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        color = TextWhite,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.login),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.dont_have_account),
                color = TextGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("registration") },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
