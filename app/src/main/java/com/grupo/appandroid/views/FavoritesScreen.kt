import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.components.LoadingIndicator
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite
import com.grupo.appandroid.utils.SessionManager
import com.grupo.appandroid.viewmodels.LoginViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FavoritesScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    // Utilizando o SessionManager para recuperar o e-mail da sessão
    val sessionManager = SessionManager(context)
    val email = sessionManager.getLoggedInEmail()

    if (email.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Email não encontrado. Faça login.", color = TextWhite)
        }
        return
    }

    val userRepository = UserRepository(context)
    val companyRepository = CompanyRepository(context)
    val isCompanyLogin = sessionManager.isCompanyLogin()
    val code = if (isCompanyLogin) {
        companyRepository.findByEmail(email)?.companyCode ?: 1
    } else {
        userRepository.findUserByEmail(email)?.userCode ?: 1
    }

    val database = AppDatabase.getDatabase(context)
    val favoriteCandidateDao = database.favoriteCandidateDao()
    val favoriteJobDao = database.favoriteDao()

    val factory = FavoritesScreenViewModelFactory(favoriteCandidateDao, favoriteJobDao, database)
    val viewModel: FavoritesScreenViewModel = viewModel(factory = factory)

    LaunchedEffect(key1 = code, key2 = isCompanyLogin) {
        viewModel.loadFavorites(
            userType = if (isCompanyLogin) UserType.COMPANY else UserType.USER,
            code = code.toString()
        )
    }
    val favoriteJobsDetails by viewModel.favoriteJobDetails.collectAsState()
    val favoriteCandidatesDetails by viewModel.favoriteCandidateDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val titleText = if (isCompanyLogin) "Candidatos Favoritados" else "Vagas Favoritadas"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = titleText,
                    color = TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                IconButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator()
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = error ?: "Erro desconhecido",
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                favoriteJobsDetails.isEmpty() && !isCompanyLogin -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nenhuma vaga favoritada",
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                favoriteCandidatesDetails.isEmpty() && isCompanyLogin -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nenhum candidato favoritado",
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    FavoritesList(
                        isCompany = isCompanyLogin,
                        viewModel = viewModel,
                        favoriteJobsDetails = favoriteJobsDetails,
                        favoriteCandidatesDetails = favoriteCandidatesDetails,
                        navController = navController
                    )
                }
            }
        }
        // NavigationBar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // Ajuste adicional para evitar colisões com a barra
        ) {
            NavigationBar(navController = navController)
        }
    }
}

@Composable
fun FavoritesList(
    isCompany: Boolean,
    viewModel: FavoritesScreenViewModel,
    favoriteJobsDetails: List<Job>,
    favoriteCandidatesDetails: List<User>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (!isCompany) {
            items(favoriteJobsDetails) { job ->
                val isFavorite = true
                JobCard(
                    job = job,
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        viewModel.toggleFavorite(job.id)
                        println("Favorite clicked in FavoritesScreen for jobId: ${job.id}")
                    },
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
        } else {
            items(favoriteCandidatesDetails) { user ->
                val isFavorite = true
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
                        println("Favorite clicked in FavoritesScreen for userCode: ${user.userCode}")
                    },
                    onClick = {
                        navController.navigate("userDetail/${user.userCode}/${user.name}/${user.email}/${user.phone}/${user.location}/${user.skills}/${user.description}")
                    }
                )
            }
        }
    }
}

// Função auxiliar já existente no seu código
fun estimateAge(academyLastYear: String?): Int {
    return academyLastYear?.toIntOrNull()?.let { lastYear ->
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val yearsSinceGraduation = currentYear - lastYear
        22 + yearsSinceGraduation
    } ?: 30
}