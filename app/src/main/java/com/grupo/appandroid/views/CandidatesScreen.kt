package com.grupo.appandroid.views

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.LoadingIndicator
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.model.BrazilianStates
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.utils.SessionManager
import com.grupo.appandroid.viewmodels.CandidatesViewModel
import java.util.Calendar

@Composable
fun CandidatesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Recupera dados da sessão
    val sessionManager = SessionManager(context)
    val isCompanyLogin = sessionManager.isCompanyLogin()
    val email = sessionManager.getLoggedInEmail()

    if (email == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Sessão expirada. Faça login novamente.", color = Color.Black)
        }
        return
    }

    // Se não for login de empresa, bloqueia o acesso
    if (!isCompanyLogin) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Acesso restrito a empresas", color = Color.Black)
        }
        return
    }

    val companyRepository = CompanyRepository(context)
    val company = companyRepository.findByEmail(email)
    val code = company?.companyCode.toString()
    val database = AppDatabase.getDatabase(context)

    // Inicializa o CandidatesViewModel para empresas
    val viewModel: CandidatesViewModel = viewModel(
        viewModelStoreOwner = navController.getViewModelStoreOwner(navController.graph.id),
        factory = CandidatesViewModelFactory(database)
    )

    LaunchedEffect(Unit) {
        if (code.isNotEmpty()) {
            viewModel.setCompanyCode(code)
            Log.d("VagasScreen", "Setting userCode: $code")
        } else {
            Log.d("VagasScreen", "userCode is null for email: $email")
        }
    }

    // Filtra os candidatos
    val filteredUsers by remember(
        viewModel.searchQuery,
        viewModel.selectedLocation,
        viewModel.selectedArea,
        viewModel.users,
        viewModel.favoriteCandidates
    ) {
        derivedStateOf {
            viewModel.users.filter { user ->
                val matchesSearch = user.name.contains(viewModel.searchQuery, ignoreCase = true)
                val matchesLocation = viewModel.selectedLocation.isEmpty() ||
                        user.location.contains(viewModel.selectedLocation, ignoreCase = true)
                val matchesArea = viewModel.selectedArea.isEmpty() ||
                        (user.academyCourse?.contains(viewModel.selectedArea, ignoreCase = true) == true) ||
                        (user.skills.contains(viewModel.selectedArea, ignoreCase = true))
                matchesSearch && matchesLocation && matchesArea
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
                isCompanyLogin = true,
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
                            text = viewModel.error ?: "Erro desconhecido",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                filteredUsers.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum usuário encontrado",
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
                        items(filteredUsers) { user ->
                            val isFavorite by remember(viewModel.favoriteCandidates) {
                                derivedStateOf { viewModel.favoriteCandidates.contains(user.userCode.toString()) }
                            }
                            CandidateCard(
                                name = user.name,
                                age = estimateAge(user.academyLastYear),
                                location = user.location,
                                area = user.academyCourse ?: user.skills,
                                experienceTime = user.description ?: "Não especificado",
                                isCompanyLogin = true,
                                isFavorite = isFavorite,
                                onFavoriteClick = {
                                    viewModel.toggleFavoriteCandidate(user.userCode.toString())
                                },
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
                contentDescription = "Voltar",
                tint = Color.White
            )
        }
    }
}

class CandidatesViewModelFactory(
    private val database: AppDatabase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidatesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidatesViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun estimateAge(academyLastYear: String?): Int {
    return academyLastYear?.toIntOrNull()?.let { lastYear ->
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearsSinceGraduation = currentYear - lastYear
        22 + yearsSinceGraduation
    } ?: 30
}
