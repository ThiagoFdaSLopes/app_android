// CandidatesScreen.kt
package com.grupo.appandroid.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.ui.theme.DarkBackground
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CandidatesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val isCompanyLogin = prefs.getString("loginType", null) == "company"

    val database = remember { AppDatabase.getDatabase(context) }
    val allUsers = remember { database.userDao().findAllUsers() }
    val allCompanies = remember { database.companyDao().findAllCompany() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("") }
    var selectedModality by remember { mutableStateOf("") }

    val filteredUsers = remember(searchQuery, selectedLocation, allUsers) {
        allUsers.filter { user ->
            val matchesSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                    user.skills.contains(searchQuery, ignoreCase = true)
            val matchesLocation = selectedLocation.isEmpty() || user.location == selectedLocation
            matchesSearch && matchesLocation
        }
    }

    val filteredCompanies = remember(searchQuery, selectedLocation, selectedModality, allCompanies) {
        allCompanies.filter { company ->
            val description = company.description ?: ""
            val matchesSearch = company.companyName.contains(searchQuery, ignoreCase = true) ||
                    description.contains(searchQuery, ignoreCase = true)
            val matchesLocation = selectedLocation.isEmpty() || company.location == selectedLocation
            val matchesModality = selectedModality.isEmpty() || description.contains(selectedModality, ignoreCase = true)
            matchesSearch && matchesLocation && matchesModality
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopHeader(
                onSearchChange = { searchQuery = it },
                onLocationChange = { selectedLocation = it },
                onModalityChange = { selectedModality = it },
                selectedLocation = selectedLocation,
                selectedModality = selectedModality
            )

            if ((isCompanyLogin && filteredUsers.isEmpty()) || (!isCompanyLogin && filteredCompanies.isEmpty())) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCompanyLogin)
                            "Nenhum candidato encontrado com os filtros selecionados"
                        else
                            "Nenhuma vaga encontrada com os filtros selecionados",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isCompanyLogin) {
                        items(filteredUsers) { user ->
                            CandidateCard(
                                name = user.name,
                                age = 0,
                                location = user.location,
                                area = user.skills,
                                experienceTime = user.description ?: "Experiência não especificada",
                                isCompanyLogin = isCompanyLogin,
                                onClick = {
                                    val encodedName = URLEncoder.encode(user.name, StandardCharsets.UTF_8.toString())
                                    val encodedEmail = URLEncoder.encode(user.email, StandardCharsets.UTF_8.toString())
                                    val encodedPhone = URLEncoder.encode(user.phone, StandardCharsets.UTF_8.toString())
                                    val encodedLocation = URLEncoder.encode(user.location, StandardCharsets.UTF_8.toString())
                                    val encodedSkills = URLEncoder.encode(user.skills, StandardCharsets.UTF_8.toString())
                                    val encodedDescription = URLEncoder.encode(user.description ?: "", StandardCharsets.UTF_8.toString())

                                    navController.navigate(
                                        "userDetail/${user.userCode}/$encodedName/$encodedEmail/$encodedPhone/$encodedLocation/$encodedSkills/$encodedDescription"
                                    )
                                }
                            )
                        }
                    } else {
                        items(filteredCompanies) { company ->
                            val jobDetails = company.description?.split(" - ") ?: listOf("", "", "")
                            val title = jobDetails.getOrNull(0) ?: ""
                            val modality = jobDetails.getOrNull(1) ?: ""
                            val salary = jobDetails.getOrNull(2) ?: ""

                            JobCard(
                                title = title,
                                company = company.companyName,
                                location = company.location,
                                modality = modality,
                                salary = salary,
                                onClick = {
                                    val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                                    val encodedCompany = URLEncoder.encode(company.companyName, StandardCharsets.UTF_8.toString())
                                    val encodedLocation = URLEncoder.encode(company.location, StandardCharsets.UTF_8.toString())
                                    val encodedModality = URLEncoder.encode(modality, StandardCharsets.UTF_8.toString())
                                    val encodedSalary = URLEncoder.encode(salary, StandardCharsets.UTF_8.toString())

                                    navController.navigate(
                                        "jobDetail/$encodedTitle/$encodedCompany/$encodedLocation/$encodedModality/$encodedSalary"
                                    )
                                }
                            )
                        }
                    }
                }
            }

            NavigationBar()
        }
    }
}
