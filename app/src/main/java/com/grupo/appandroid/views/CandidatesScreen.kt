package com.grupo.appandroid.views

import Job
import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.Category
import com.grupo.appandroid.model.BrazilianStates
import com.grupo.appandroid.model.User
import com.grupo.appandroid.service.RetrofitClient
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.AmberPrimary
import kotlinx.coroutines.Dispatchers
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

@Composable
fun CandidatesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val isCompanyLogin = prefs.getString("loginType", null) == "company"

    // Banco de dados Room
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    // Estados
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var jobs by remember { mutableStateOf<List<Job>>(emptyList()) }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedArea by remember { mutableStateOf("") }
    var resultsPerPage by remember { mutableStateOf(20) }

    val coroutineScope = rememberCoroutineScope()

    // Função para buscar usuários com paginação e filtro por área
    suspend fun fetchUsers(page: Int) {
        isLoading = true
        error = null
        try {
            withContext(Dispatchers.IO) {
                val offset = (page - 1) * resultsPerPage
                val filteredArea = if (selectedArea.isEmpty()) null else "%$selectedArea%"
                users = userDao.findUsersPaginated(filteredArea, resultsPerPage, offset)
                val totalUsers = userDao.countUsersByArea(filteredArea)
                totalPages = (totalUsers + resultsPerPage - 1) / resultsPerPage
                if (totalPages == 0) totalPages = 1
            }
        } catch (e: Exception) {
            error = "Erro ao buscar usuários: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Função para buscar vagas (mantida como está)
    suspend fun fetchJobs(page: Int) {
        isLoading = true
        error = null
        try {
            val response = RetrofitClient.jobService.searchJobs(
                page = page,
                category = selectedCategory.takeIf { it.isNotEmpty() },
                resultsPerPage = resultsPerPage
            )
            if (response.isSuccessful) {
                val jobResponse = response.body()
                if (jobResponse != null) {
                    jobs = jobResponse.results ?: emptyList()
                    val totalResults = jobResponse.count ?: 0
                    totalPages = (totalResults + (resultsPerPage - 1)) / resultsPerPage
                    if (totalPages == 0) totalPages = 1
                }
            } else {
                error = "Failed to fetch jobs: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Fetch inicial
    LaunchedEffect(Unit) {
        if (isCompanyLogin) {
            fetchUsers(1)
        } else {
            try {
                val response = RetrofitClient.jobService.getCategories()
                if (response.isSuccessful) {
                    categories = response.body()?.results ?: emptyList()
                }
            } catch (e: Exception) {
                error = "Error fetching categories: ${e.message}"
            }
            fetchJobs(1)
        }
    }

    // Atualizar quando filtros ou página mudarem
    LaunchedEffect(selectedArea, selectedLocation, searchQuery, currentPage, resultsPerPage) {
        if (isCompanyLogin && currentPage > 0) {
            fetchUsers(currentPage)
        }
    }

    LaunchedEffect(selectedCategory, currentPage, resultsPerPage) {
        if (!isCompanyLogin && currentPage > 0) {
            fetchJobs(currentPage)
        }
    }

    val filteredUsers = remember(searchQuery, selectedLocation, selectedArea, users) {
        users.filter { user ->
            val matchesSearch = user.name.contains(searchQuery, ignoreCase = true)
            val matchesLocation = selectedLocation.isEmpty() || user.location.contains(selectedLocation, ignoreCase = true)
            val matchesArea = selectedArea.isEmpty() ||
                    (user.academyCourse?.contains(selectedArea, ignoreCase = true) == true) ||
                    (user.skills.contains(selectedArea, ignoreCase = true))
            matchesSearch && matchesLocation && matchesArea
        }
    }

    val filteredJobs = remember(searchQuery, selectedLocation, jobs) {
        jobs.filter { job ->
            val matchesSearch = job.title.contains(searchQuery, ignoreCase = true) ||
                    job.description.contains(searchQuery, ignoreCase = true)
            val matchesLocation = selectedLocation.isEmpty() ||
                    job.location.display_name.contains(selectedLocation, ignoreCase = true)
            matchesSearch && matchesLocation
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
                onCategoryChange = {
                    selectedCategory = it
                    currentPage = 1
                    coroutineScope.launch {
                        if (!isCompanyLogin) fetchJobs(1)
                    }
                },
                onLocationChange = { selectedLocation = it },
                onResultsPerPageChange = {
                    resultsPerPage = it
                    currentPage = 1
                    coroutineScope.launch {
                        if (isCompanyLogin) fetchUsers(1) else fetchJobs(1)
                    }
                },
                onAreaChange = {
                    selectedArea = it
                    currentPage = 1
                    coroutineScope.launch {
                        if (isCompanyLogin) fetchUsers(1)
                    }
                },
                selectedCategory = selectedCategory,
                selectedLocation = selectedLocation,
                selectedResultsPerPage = resultsPerPage,
                selectedArea = selectedArea,
                categories = categories,
                locations = BrazilianStates.states,
                isCompanyLogin = isCompanyLogin,
                areas = listOf("TI", "Engenharia", "Saúde", "Marketing")
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = error ?: "Unknown error occurred",
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

                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (currentPage > 1) {
                                                currentPage--
                                                coroutineScope.launch {
                                                    fetchUsers(currentPage)
                                                }
                                            }
                                        },
                                        enabled = currentPage > 1,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "Anterior",
                                            tint = if (currentPage > 1) Color.White else Color.Gray
                                        )
                                    }

                                    Text(
                                        text = "$currentPage de $totalPages",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            if (currentPage < totalPages) {
                                                currentPage++
                                                coroutineScope.launch {
                                                    fetchUsers(currentPage)
                                                }
                                            }
                                        },
                                        enabled = currentPage < totalPages,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowRight,
                                            contentDescription = "Próxima",
                                            tint = if (currentPage < totalPages) Color.White else Color.Gray
                                        )
                                    }
                                }
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

                                        navController.navigate(
                                            "jobDetail/$encodedTitle/$encodedCompany/$encodedLocation/$encodedModality/$encodedDescription"
                                        )
                                    }
                                )
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
                                        onClick = {
                                            if (currentPage > 1) {
                                                currentPage--
                                                coroutineScope.launch {
                                                    fetchJobs(currentPage)
                                                }
                                            }
                                        },
                                        enabled = currentPage > 1,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "Anterior",
                                            tint = if (currentPage > 1) Color.White else Color.Gray
                                        )
                                    }

                                    Text(
                                        text = "$currentPage de $totalPages",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            if (currentPage < totalPages) {
                                                currentPage++
                                                coroutineScope.launch {
                                                    fetchJobs(currentPage)
                                                }
                                            }
                                        },
                                        enabled = currentPage < totalPages,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowRight,
                                            contentDescription = "Próxima",
                                            tint = if (currentPage < totalPages) Color.White else Color.Gray
                                        )
                                    }
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

// Função auxiliar para estimar a idade com base no último ano acadêmico
fun estimateAge(academyLastYear: String?): Int {
    return academyLastYear?.toIntOrNull()?.let { lastYear ->
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val yearsSinceGraduation = currentYear - lastYear
        // Assume que a pessoa tinha 22 anos ao se formar (média comum)
        22 + yearsSinceGraduation
    } ?: 30 // Idade padrão se não houver dados
}