package com.grupo.appandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextGray
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun LoginScreen(navController: NavController) {
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
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )

            CustomTextField(
                label = stringResource(id = R.string.password),
                isPassword = true,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Password
            )

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
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberPrimary,
                    contentColor = TextWhite
                )
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
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