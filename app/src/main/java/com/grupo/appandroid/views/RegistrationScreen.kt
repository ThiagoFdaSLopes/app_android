package com.grupo.appandroid.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextArea
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.components.RegistrationTabRow
import com.grupo.appandroid.components.TabItem
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextGray
import com.grupo.appandroid.ui.theme.TextWhite
import com.grupo.appandroid.viewmodels.RegistrationViewModel

@Composable
fun RegistrationScreen(navController: NavController, viewModel: RegistrationViewModel) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val userRepository = UserRepository(context)
    val companyRepository = CompanyRepository(context)
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
                text = stringResource(id = R.string.sign_up),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Tabs
            val tabs = listOf(
                TabItem(stringResource(id = R.string.people), Icons.Default.Person),
                TabItem(stringResource(id = R.string.company), Icons.Default.Home)
            )

            RegistrationTabRow(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                tabs = tabs
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form content based on selected tab
            if (selectedTabIndex == 0) {
                PeopleRegistrationForm(navController = navController, viewModel = viewModel, userRepository = userRepository, companyRepository = companyRepository)
            } else {
                CompanyRegistrationForm( navController = navController, viewModel = viewModel, userRepository = userRepository, companyRepository = companyRepository)
            }
        }
    }
}

@Composable
fun PeopleRegistrationForm(navController: NavController, viewModel: RegistrationViewModel, userRepository: UserRepository, companyRepository: CompanyRepository) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(label = stringResource(id = R.string.fullName), value = viewModel.name.value, onValueChange = { name ->  viewModel.name.value = name })
        CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email, value = viewModel.email.value, onValueChange = { email ->  viewModel.email.value = email })
        CustomTextField(label = stringResource(id = R.string.phone), keyboardType = KeyboardType.Phone, value = viewModel.phone.value, onValueChange = { phone ->  viewModel.phone.value = phone })
        CustomTextField(label = stringResource(id = R.string.password), isPassword = true, value = viewModel.password.value, onValueChange = { password ->  viewModel.password.value = password })
        CustomTextField(label = stringResource(id = R.string.confirm_password), isPassword = true, value = viewModel.confirmPassword.value, onValueChange = { confirmPassword ->  viewModel.confirmPassword.value = confirmPassword })
        CustomTextField(label = stringResource(id = R.string.document), value = viewModel.document.value, onValueChange = { document ->  viewModel.document.value = document })
        CustomTextField(label = stringResource(id = R.string.location), value = viewModel.location.value, onValueChange = { location ->  viewModel.location.value = location })
        CustomTextField(label = stringResource(id = R.string.skills), value = viewModel.skills.value, onValueChange = { skills ->  viewModel.skills.value = skills })

        Text(
            text = stringResource(id = R.string.biography),
            color = TextWhite,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        CustomTextArea(label = stringResource(id = R.string.description), value = viewModel.description.value, onValueChange = { description ->  viewModel.description.value = description })

        Text(
            text = stringResource(id = R.string.academy_background),
            color = TextWhite,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CustomTextField(label = stringResource(id = R.string.academy_level), value = viewModel.academyLevel.value, onValueChange = { academyLevel ->  viewModel.academyLevel.value = academyLevel })
        CustomTextField(label = stringResource(id = R.string.academy_course), value = viewModel.academyCourse.value, onValueChange = { academyCourse ->  viewModel.academyCourse.value = academyCourse })
        CustomTextField(label = stringResource(id = R.string.academy_institution), value = viewModel.academyInstitution.value, onValueChange = { academyInstitution ->  viewModel.academyInstitution.value = academyInstitution })
        CustomTextField(label = stringResource(id = R.string.academy_year_complete), value = viewModel.academyLastYear.value, onValueChange = { academyLastYear ->  viewModel.academyLastYear.value = academyLastYear })

        // Validação: verifica se todos os campos obrigatórios estão preenchidos e se as senhas coincidem
        val isFormValid = viewModel.name.value.isNotBlank() &&
                viewModel.email.value.isNotBlank() &&
                viewModel.phone.value.isNotBlank() &&
                viewModel.password.value.isNotBlank() &&
                viewModel.confirmPassword.value.isNotBlank() &&
                viewModel.location.value.isNotBlank() &&
                viewModel.skills.value.isNotBlank() &&
                viewModel.description.value.isNotBlank() &&
                (viewModel.password.value == viewModel.confirmPassword.value)

        SignUpButton(registrationViewModel = viewModel, userRepository= userRepository, type = "user", companyRepository = companyRepository, isFormValid = isFormValid)

        SignInText(navController)
    }
}

@Composable
fun CompanyRegistrationForm(navController: NavController, viewModel: RegistrationViewModel, userRepository: UserRepository, companyRepository: CompanyRepository) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(label = stringResource(id = R.string.company_name), value = viewModel.name.value, onValueChange = { name ->  viewModel.name.value = name })
        CustomTextField(label = stringResource(id = R.string.email), keyboardType = KeyboardType.Email, value = viewModel.email.value, onValueChange = { email ->  viewModel.email.value = email })
        CustomTextField(label = stringResource(id = R.string.phone), keyboardType = KeyboardType.Phone, value = viewModel.phone.value, onValueChange = { phone ->  viewModel.phone.value = phone })
        CustomTextField(label = stringResource(id = R.string.password), isPassword = true, value = viewModel.password.value, onValueChange = { password ->  viewModel.password.value = password })
        CustomTextField(label = stringResource(id = R.string.confirm_password), isPassword = true, value = viewModel.confirmPassword.value, onValueChange = { confirmPassword ->  viewModel.confirmPassword.value = confirmPassword })
        CustomTextField(label = stringResource(id = R.string.document), value = viewModel.document.value, onValueChange = { document ->  viewModel.document.value = document })
        CustomTextField(label = stringResource(id = R.string.location), value = viewModel.location.value, onValueChange = { location ->  viewModel.location.value = location })
        CustomTextField(label = stringResource(id = R.string.industry), value = viewModel.industry.value, onValueChange = { industry ->  viewModel.industry.value = industry })

        Text(
            text = stringResource(id = R.string.company_description),
            color = TextWhite,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        CustomTextArea(label = stringResource(id = R.string.description), value = viewModel.description.value, onValueChange = { academyLastYear ->  viewModel.description.value = academyLastYear })

        // Validação: verifica se todos os campos obrigatórios estão preenchidos e se as senhas coincidem
        val isFormValid = viewModel.name.value.isNotBlank() &&
                viewModel.email.value.isNotBlank() &&
                viewModel.phone.value.isNotBlank() &&
                viewModel.password.value.isNotBlank() &&
                viewModel.confirmPassword.value.isNotBlank() &&
                viewModel.location.value.isNotBlank() &&
                viewModel.industry.value.isNotBlank() &&
                viewModel.description.value.isNotBlank() &&
                (viewModel.password.value == viewModel.confirmPassword.value)

        SignUpButton(registrationViewModel = viewModel, userRepository = userRepository, type = "company", companyRepository = companyRepository, isFormValid = isFormValid)

        SignInText(navController)
    }
}

@Composable
fun SignUpButton(registrationViewModel: RegistrationViewModel, userRepository: UserRepository,companyRepository: CompanyRepository, type: String, isFormValid: Boolean) {
    var user: User
    var company: Company

    // Função de Registrar
    fun register() {
        if(type === "user") {
            user = registrationViewModel.createUser()
            userRepository.save(user)
        }

        if(type === "company") {
            company = registrationViewModel.createCompany()
            companyRepository.save(company)
        }
    }

    Button(
        onClick = { register() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AmberPrimary
        ),
        shape = RoundedCornerShape(4.dp),
        enabled = isFormValid
    ) {
        Text(
            text = stringResource(id = R.string.sign_up),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SignInText(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.have_an_account),
            color = TextGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { navController.navigate("login") }
        )
        TextButton(onClick = { navController.navigate("login") }) {
            Text(
                text = stringResource(id = R.string.sign_in),
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
        }
    }
}