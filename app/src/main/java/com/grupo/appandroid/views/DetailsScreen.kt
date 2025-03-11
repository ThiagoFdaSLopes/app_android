package com.grupo.appandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.model.User
import com.grupo.appandroid.model.Company

@Composable
fun UserDetailScreen(
    user: User,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1E21))
            .systemBarsPadding()
            .padding(top = 22.dp)
            .padding(16.dp)
    ) {
        // Profile Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name and Academic Info
            Text(
                text = user.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            user.academyCourse?.let { course ->
                Text(
                    text = course,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Info Items
        InfoItem(icon = "📱", label = "Telefone:", value = user.phone)
        InfoItem(icon = "📍", label = "Localização:", value = user.location)
        InfoItem(icon = "🎓", label = "Formação:", value = "${user.academyLevel ?: "Não informado"}")
        user.academyInstitution?.let { institution ->
            InfoItem(icon = "🏛️", label = "Instituição:", value = institution)
        }
        user.academyLastYear?.let { year ->
            InfoItem(icon = "📅", label = "Ano de Conclusão:", value = year)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Skills Section
        Text(
            text = "Habilidades:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = user.skills,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )

        // Description Section
        user.description?.let { description ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sobre:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("FAVORITAR CANDIDATO", color = Color.Black)
        }

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("CONTATO")
        }

        NavigationBar(
            onSettingsClick = { navController.navigate("SettingsScreen") },
            onPeopleClick = { navController.navigate("PeopleScreen") },
            onBriefcaseClick = { navController.navigate("BriefcaseScreen") },
            onBellClick = { navController.navigate("NotificationsScreen") },
            onStarClick = { navController.navigate("FavoritesScreen") }
        )
    }
}

@Composable
fun CompanyDetailScreen(
    company: Company,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1E21))
            .systemBarsPadding()
            .padding(top = 22.dp)
            .padding(16.dp)
    ) {
        // Company Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Company Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = company.companyName,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = company.industry,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Company Details
        InfoItem(icon = "📱", label = "Telefone:", value = company.phone)
        InfoItem(icon = "📍", label = "Localização:", value = company.location)
        InfoItem(icon = "🏢", label = "Setor:", value = company.industry)
        InfoItem(icon = "📄", label = "CNPJ:", value = company.document)

        Spacer(modifier = Modifier.height(24.dp))

        // Description Section
        company.description?.let { description ->
            Text(
                text = "Sobre a empresa:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("SEGUIR EMPRESA", color = Color.Black)
        }

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("VER VAGAS")
        }

        NavigationBar(
            onSettingsClick = { navController.navigate("SettingsScreen") },
            onPeopleClick = { navController.navigate("PeopleScreen") },
            onBriefcaseClick = { navController.navigate("BriefcaseScreen") },
            onBellClick = { navController.navigate("NotificationsScreen") },
            onStarClick = { navController.navigate("FavoritesScreen") }
        )
    }
}

@Composable
fun JobDetailScreen(
    title: String,
    company: String,
    location: String,
    modality: String,
    salary: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1E21))
            .systemBarsPadding()
            .padding(top = 22.dp)
            .padding(16.dp)
    ) {
        // Job Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Company Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = company,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Job Details
        InfoItem(icon = "📍", label = "Localização:", value = location)
        InfoItem(icon = "💼", label = "Modalidade:", value = modality)
        InfoItem(icon = "💰", label = "Salário:", value = salary)

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("SALVAR VAGA", color = Color.Black)
        }

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("CANDIDATAR-SE", color = Color.Black)
        }

        NavigationBar(
            onSettingsClick = { navController.navigate("SettingsScreen") },
            onPeopleClick = { navController.navigate("PeopleScreen") },
            onBriefcaseClick = { navController.navigate("BriefcaseScreen") },
            onBellClick = { navController.navigate("NotificationsScreen") },
            onStarClick = { navController.navigate("FavoritesScreen") }
        )
    }
}


@Composable
private fun InfoItem(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label $value",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
