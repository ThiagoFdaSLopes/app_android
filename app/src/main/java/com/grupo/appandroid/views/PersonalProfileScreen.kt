package com.grupo.appandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.components.CustomTextField
import com.grupo.appandroid.components.MinimalDialog
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.viewmodels.PersonalProfileScreenViewModel

@Composable
fun PersonalProfileScreen(user: User, navController: NavController) {
    // Obtém a mesma instância da ViewModel durante o ciclo de vida da tela
    val viewModel: PersonalProfileScreenViewModel = viewModel()

    // Inicializa os dados do usuário apenas na primeira vez que a user.userCode mudar
    LaunchedEffect(key1 = user.userCode) {
        viewModel.setUserData(user)
    }

    val localContext = LocalContext.current
    val userRepository = UserRepository(localContext)

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
                painter = painterResource(id = R.drawable.people),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = viewModel.name.value,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Candidate",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.fullName),
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                color = Color.White
            )
            CustomTextField(
                label = stringResource(id = R.string.fullName),
                value = viewModel.name.value,
                onValueChange = { name -> viewModel.name.value = name }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.email),
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                color = Color.White
            )
            CustomTextField(
                label = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email,
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.phone),
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                color = Color.White
            )
            CustomTextField(
                label = stringResource(id = R.string.phone),
                keyboardType = KeyboardType.Phone,
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.value = it }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.location),
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                color = Color.White
            )
            CustomTextField(
                label = stringResource(id = R.string.location),
                value = viewModel.location.value,
                onValueChange = { viewModel.location.value = it }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { val userUpdated = viewModel.updateUser()
                            userRepository.update((userUpdated))
                    viewModel.showSuccess.value = true
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save_changes),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberPrimary),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Voltar",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            MinimalDialog(
                onDismissRequest = {},
                onConfirmation = { viewModel.showSuccess.value = false
                    navController.navigate("home") },
                dialogText = "Registro Atualizado Com Sucesso",
                dialogTitle = "Cadastro de Usuario",
                textButton = "Concluir",
                showDialog = viewModel.showSuccess.value)
        }
    }
}
