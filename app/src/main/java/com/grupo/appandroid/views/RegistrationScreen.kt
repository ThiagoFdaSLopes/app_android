package com.grupo.appandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextArea
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.components.RegistrationTabRow
import com.grupo.appandroid.components.TabItem
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextGray
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun RegistrationScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Sign Up Title
            Text(
                text = stringResource(id = R.string.sign_up ),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Tabs
            val tabs = listOf(
                TabItem(stringResource(id = R.string.people ), Icons.Default.Person),
                TabItem(stringResource(id = R.string.company ), Icons.Default.Home)
            )

            RegistrationTabRow(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                tabs = tabs
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form content based on selected tab
            if (selectedTabIndex == 0) {
                PeopleRegistrationForm()
            } else {
                CompanyRegistrationForm()
            }
        }
    }
}

@Composable
fun PeopleRegistrationForm() {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(label = stringResource(id = R.string.fullName))
        CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email)
        CustomTextField(label = stringResource(id = R.string.phone), keyboardType = KeyboardType.Phone)
        CustomTextField(label = stringResource(id = R.string.password), isPassword = true)
        CustomTextField(label = stringResource(id = R.string.confirm_password), isPassword = true)
        CustomTextField(label = stringResource(id = R.string.document))
        CustomTextField(label = stringResource(id = R.string.location))
        CustomTextField(label = stringResource(id = R.string.skills))

        Text(
            text = stringResource(id = R.string.biography),
            color = TextWhite,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        CustomTextArea(label = stringResource(id = R.string.description))

        Text(
            text = stringResource(id = R.string.academy_background),
            color = TextWhite,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CustomTextField(label = stringResource(id = R.string.academy_level))
        CustomTextField(label = stringResource(id = R.string.academy_course))
        CustomTextField(label = stringResource(id = R.string.academy_institution))
        CustomTextField(label = stringResource(id = R.string.academy_year_complete))

        SignUpButton()

        SignInText()
    }
}

@Composable
fun CompanyRegistrationForm() {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(label = stringResource(id = R.string.company_name))
        CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email)
        CustomTextField(label = stringResource(id = R.string.phone), keyboardType = KeyboardType.Phone)
        CustomTextField(label = stringResource(id = R.string.password), isPassword = true)
        CustomTextField(label = stringResource(id = R.string.confirm_password), isPassword = true)
        CustomTextField(label = stringResource(id = R.string.document))
        CustomTextField(label = stringResource(id = R.string.location))
        CustomTextField(label = stringResource(id = R.string.industry))

        Text(
            text = stringResource(id = R.string.company_description),
            color = TextWhite,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        CustomTextArea(label = stringResource(id = R.string.description))

        SignUpButton()

        SignInText()
    }
}

@Composable
fun SignUpButton() {
    Button(
        onClick = { /* TODO: Handle sign up */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AmberPrimary
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.sign_up),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SignInText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.have_an_account),
            color = TextGray,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = { /* TODO: Navigate to sign in */ }) {
            Text(
                text = stringResource(id = R.string.sign_in),
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
        }
    }
}