package com.grupo.appandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.components.LoadingIndicator
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.BrazilianStates
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.utils.SessionManager
import com.grupo.appandroid.viewmodels.VagasScreenViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun VagasScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Recupera dados da sessão
    val sessionManager = SessionManager(context)
    val email = sessionManager.getLoggedInEmail()
    val isCompanyLogin = sessionManager.isCompanyLogin()
    val userRepository = UserRepository(context)
    val user = userRepository.findUserByEmail(email!!)
    val userCode = user?.userCode


    if (email == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.session_expired), color = Color.White)
        }
        return
    }

    // Se o usuário for empresa, bloqueia o acesso, pois essa área é exclusiva para candidatos
    if (isCompanyLogin) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.session_expired), color = Color.White)
        }
        return
    }

    val database = AppDatabase.getDatabase(context)
    // Instancia o VagasScreenViewModel por meio do Factory
    val viewModel: VagasScreenViewModel = viewModel(
        viewModelStoreOwner = navController.getViewModelStoreOwner(navController.graph.id),
        factory = VagasScreenViewModelFactory(database, userCode = userCode.toString())
    )

    // Filtra as vagas com base na busca e na localização selecionada
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
        Column(modifier = Modifier.fillMaxSize()) {
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
                isCompanyLogin = false,
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
                            text = viewModel.error ?: stringResource(id = R.string.unknow_error),
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                filteredJobs.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_job_found),
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
                        items(filteredJobs) { job ->
                            val isFavorite by remember(viewModel.favorites) {
                                derivedStateOf { viewModel.favorites.contains(job.id) }
                            }
                            JobCard(
                                job = job,
                                isFavorite = isFavorite,
                                onFavoriteClick = { viewModel.toggleFavoriteJob(job.id) },
                                onClick = {
                                    val encodedJobId = URLEncoder.encode(job.id, StandardCharsets.UTF_8.toString())
                                    val encodedTitle = URLEncoder.encode(job.title, StandardCharsets.UTF_8.toString())
                                    val encodedCompany = URLEncoder.encode(job.company.display_name, StandardCharsets.UTF_8.toString())
                                    val encodedLocation = URLEncoder.encode(job.location.display_name, StandardCharsets.UTF_8.toString())
                                    val encodedModality = URLEncoder.encode(job.contract_time ?: "Não especificado", StandardCharsets.UTF_8.toString())
                                    val encodedDescription = URLEncoder.encode(job.description, StandardCharsets.UTF_8.toString())
                                    navController.navigate("jobDetail/$encodedJobId/$encodedTitle/$encodedCompany/$encodedLocation/$encodedModality/$encodedDescription")
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
                                    onClick = { viewModel.previousPage() },
                                    enabled = viewModel.currentPage > 1,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowLeft,
                                        contentDescription = stringResource(id = R.string.back),
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
                                        contentDescription = stringResource(id = R.string.next),
                                        tint = if (viewModel.currentPage < viewModel.totalPages) Color.White else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            NavigationBar(navController = navController)
        }
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                tint = Color.White
            )
        }
    }
}

class VagasScreenViewModelFactory(
    private val database: AppDatabase,
    private val userCode: String // Adiciona o userCode como parâmetro
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VagasScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VagasScreenViewModel(database, userCode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}