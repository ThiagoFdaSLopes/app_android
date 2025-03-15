import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.FavoriteItem
import com.grupo.appandroid.ui.theme.DarkBackground
import com.grupo.appandroid.ui.theme.TextWhite

@Composable
fun FavoritesScreen(
    navController: NavController,
    isCompany: Boolean
) {
    val context = LocalContext.current

    // Obtendo as SharedPreferences
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val email = prefs.getString("loggedInEmail", null)

    // Se o email for nulo, trate o caso (redirecione para login ou exiba mensagem)
    if (email.isNullOrEmpty()) {
        // Exemplo: Tela de erro ou redirecionamento
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Email não encontrado. Faça login.", color = TextWhite)
        }
        return
    }

    // Crie as instâncias dos repositórios para obter os códigos
    val userRepository = UserRepository(context)
    val companyRepository = CompanyRepository(context)

    // Define se o login é de usuário (quando não for login de empresa)
    val isUser = !isCompany
    val code = if (isUser) {
        userRepository.findUserByEmail(email)?.userCode ?: 1
    } else {
        companyRepository.findByEmail(email)?.companyCode ?: 1
    }

    // Obtenha as instâncias dos DAOs a partir do banco de dados
    // Exemplo: MyDatabase é sua classe de banco de dados singleton
    val database = AppDatabase.getDatabase(context)
    val favoriteCandidateDao = database.favoriteCandidateDao()
    val favoriteJobDao = database.favoriteDao()

    // Crie o Factory e obtenha a ViewModel
    val factory = FavoritesScreenViewModelFactory(favoriteCandidateDao, favoriteJobDao)
    val viewModel: FavoritesScreenViewModel = viewModel(factory = factory)

    // Carrega os favoritos conforme o tipo de usuário
    LaunchedEffect(key1 = code, key2 = isCompany) {
        viewModel.loadFavorites(
            userType = if (isCompany) UserType.COMPANY else UserType.USER,
            code = code.toString()
        )
    }

    // Observe os fluxos da ViewModel
    val favoriteCandidates by viewModel.favoriteCandidates.collectAsState()
    val favoriteJobs by viewModel.favoriteJobs.collectAsState()

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
            val titleText = if (isCompany) "Candidatos Favoritados" else "Vagas Favoritadas"
            Text(
                text = titleText,
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            FavoritesList(
                isCompany = isCompany,
                viewModel = viewModel,
                onCardClick = { navController.navigate("DetailsScreen") },
                onHeartClick = { /* Lógica para atualizar ou desfavoritar */ }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            NavigationBar(
                onSettingsClick = { navController.navigate("SettingsScreen") },
                onPeopleClick = { navController.navigate("PeopleScreen") },
                onBriefcaseClick = { navController.navigate("BriefcaseScreen") },
                onBellClick = { navController.navigate("NotificationsScreen") },
                onStarClick = { navController.navigate("FavoritesScreen") }
            )
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FavoritesList(
    isCompany: Boolean,
    viewModel: FavoritesScreenViewModel,
    onCardClick: () -> Unit,
    onHeartClick: () -> Unit
) {
    // Mapeia os dados para FavoriteItem conforme o tipo do usuário
    val favoriteItems: List<FavoriteItem> = if (isCompany) {
        viewModel.favoriteCandidates.value.map { candidate ->
            FavoriteItem.CandidateFavorite(candidate)
        }
    } else {
        viewModel.favoriteJobs.value.map { job ->
            FavoriteItem.JobFavorite(job)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(favoriteItems) { favoriteItem ->
            FavoriteCard(
                favoriteItem = favoriteItem,
                onCardClick = onCardClick,
                onHeartClick = onHeartClick
            )
        }
    }
}
