package com.grupo.appandroid.views

import android.content.Context
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.BrazilianStates
import com.grupo.appandroid.ui.theme.AmberPrimary
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.viewmodels.CandidatesViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
// Factory para criar a ViewModel
import androidx.lifecycle.ViewModelProvider
@Composable
fun CandidatesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val isCompanyLogin = prefs.getString("loginType", null) == "company"
    val database = AppDatabase.getDatabase(context)

    val viewModel: CandidatesViewModel = viewModel(
        factory = CandidatesViewModelFactory(database, isCompanyLogin)
    )

    val filteredUsers by remember(viewModel.searchQuery, viewModel.selectedLocation, viewModel.selectedArea, viewModel.users) {
        derivedStateOf {
            viewModel.users.filter { user ->
                val matchesSearch = user.name.contains(viewModel.searchQuery, ignoreCase = true)
                val matchesLocation = viewModel.selectedLocation.isEmpty() || user.location.contains(viewModel.selectedLocation, ignoreCase = true)
                val matchesArea = viewModel.selectedArea.isEmpty() ||
                        (user.academyCourse?.contains(viewModel.selectedArea, ignoreCase = true) == true) ||
                        (user.skills.contains(viewModel.selectedArea, ignoreCase = true))
                matchesSearch && matchesLocation && matchesArea
            }
        }
    }

    val filteredJobs by remember(viewModel.searchQuery, viewModel.selectedLocation, viewModel.jobs) {
        derivedStateOf {
            viewModel.jobs.filter { job ->
                val matchesSearch = job.title.contains(viewModel.searchQuery, ignoreCase = true) ||
                        job.description.contains(viewModel.searchQuery, ignoreCase = true)
                val matchesLocation = viewModel.selectedLocation.isEmpty() ||
                        job.location.display_name.contains(viewModel.selectedLocation, ignoreCase = true)
                matchesSearch && matchesLocation
            }
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
                onSearchChange = viewModel::updateSearchQuery,
                onCategoryChange = viewModel::updateCategory,
                onLocationChange = viewModel::updateLocation,
                onResultsPerPageChange = viewModel::updateResultsPerPage,
                onAreaChange = viewModel::updateArea,
                selectedCategory = viewModel.selectedCategory,
                selectedLocation = viewModel.selectedLocation,
                selectedResultsPerPage = viewModel.resultsPerPage,
                selectedArea = viewModel.selectedArea,
                categories = viewModel.categories,
                locations = BrazilianStates.states,
                isCompanyLogin = isCompanyLogin,
                areas = listOf("TI", "Engenharia", "Saúde", "Marketing")
            )

            when {
                viewModel.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator()
                    }
                }
                viewModel.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = viewModel.error ?: "Unknown error",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                (isCompanyLogin && filteredUsers.isEmpty()) || (!isCompanyLogin && filteredJobs.isEmpty()) -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCompanyLogin) "Nenhum usuário encontrado" else "Nenhuma vaga encontrada",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
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
                                    age = estimateAge(user.academyLastYear),
                                    location = user.location,
                                    area = user.academyCourse ?: user.skills,
                                    experienceTime = user.description ?: "Não especificado",
                                    isCompanyLogin = true,
                                    onClick = {
                                        navController.navigate("userDetail/${user.userCode}/${user.name}/${user.email}/${user.phone}/${user.location}/${user.skills}/${user.description}")
                                    }
                                )
                            }
                        } else {
                            items(filteredJobs) { job ->
                                JobCard(
                                    job = job,
                                    onClick = {
                                        val encodedTitle = URLEncoder.encode(job.title, StandardCharsets.UTF_8.toString())
                                        val encodedCompany = URLEncoder.encode(job.company.display_name, StandardCharsets.UTF_8.toString())
                                        val encodedLocation = URLEncoder.encode(job.location.display_name, StandardCharsets.UTF_8.toString())
                                        val encodedModality = URLEncoder.encode(job.contract_time ?: "Não especificado", StandardCharsets.UTF_8.toString())
                                        val encodedDescription = URLEncoder.encode(job.description, StandardCharsets.UTF_8.toString())
                                        navController.navigate("jobDetail/$encodedTitle/$encodedCompany/$encodedLocation/$encodedModality/$encodedDescription")
                                    }
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.previousPage() },
                                    enabled = viewModel.currentPage > 1,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowLeft,
                                        contentDescription = "Anterior",
                                        tint = if (viewModel.currentPage > 1) Color.White else Color.Gray
                                    )
                                }

                                Text(
                                    text = "${viewModel.currentPage} de ${viewModel.totalPages}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                IconButton(
                                    onClick = { viewModel.nextPage() },
                                    enabled = viewModel.currentPage < viewModel.totalPages,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Próxima",
                                        tint = if (viewModel.currentPage < viewModel.totalPages) Color.White else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            NavigationBar()
        }
    }
}



class CandidatesViewModelFactory(
    private val database: AppDatabase,
    private val isCompanyLogin: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidatesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidatesViewModel(database, isCompanyLogin) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Mantenha LoadingIndicator e estimateAge como estavam
@Composable
private fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .scale(scale),
            color = AmberPrimary,
            strokeWidth = 4.dp
        )
        Text(
            text = "Carregando vagas...",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun estimateAge(academyLastYear: String?): Int {
    return academyLastYear?.toIntOrNull()?.let { lastYear ->
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val yearsSinceGraduation = currentYear - lastYear
        22 + yearsSinceGraduation
    } ?: 30
}