import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.FavoriteCandidate
import com.grupo.appandroid.database.dao.FavoriteCandidateDao
import com.grupo.appandroid.database.dao.FavoriteJob
import com.grupo.appandroid.database.dao.FavoriteJobDao
import com.grupo.appandroid.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import kotlinx.coroutines.flow.first

enum class UserType {
    USER, COMPANY
}




class FavoritesScreenViewModel(
    private val favoriteCandidateDao: FavoriteCandidateDao,
    private val favoriteJobDao: FavoriteJobDao,
    private val database: AppDatabase
) : ViewModel() {
    private val _favoriteCandidates = MutableStateFlow<List<FavoriteCandidate>>(emptyList())
    val favoriteCandidates: StateFlow<List<FavoriteCandidate>> = _favoriteCandidates

    private val _favoriteJobs = MutableStateFlow<List<FavoriteJob>>(emptyList())
    val favoriteJobs: StateFlow<List<FavoriteJob>> = _favoriteJobs

    private val _favoriteJobDetails = MutableStateFlow<List<Job>>(emptyList())
    val favoriteJobDetails: StateFlow<List<Job>> = _favoriteJobDetails

    private val _favoriteCandidateDetails = MutableStateFlow<List<User>>(emptyList())
    val favoriteCandidateDetails: StateFlow<List<User>> = _favoriteCandidateDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFavorites(userType: UserType, code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("FavoritesScreenViewModel", "Iniciando carregamento para userType: $userType, code: $code")
            _error.value = null
            try {
                when (userType) {
                    UserType.USER -> {
                        val favorites = favoriteJobDao.getFavoritesByUser(code).first() // Carrega apenas o primeiro valor
                        _favoriteJobs.value = favorites
                        _favoriteJobDetails.value = favorites.map { favoriteJob ->
                            Job(
                                id = favoriteJob.jobId,
                                title = favoriteJob.title,
                                description = favoriteJob.description,
                                company = Company(display_name = favoriteJob.companyName),
                                location = Location(display_name = favoriteJob.locationName),
                                contract_time = favoriteJob.contractTime
                            )
                        }
                        Log.d("FavoritesScreenViewModel", "Vagas favoritadas carregadas: ${favorites.size}")
                    }
                    UserType.COMPANY -> {
                        val favorites = favoriteCandidateDao.getFavoritesByCompany(code).first()
                        _favoriteCandidates.value = favorites
                        loadFavoriteCandidateDetails(favorites.map { it.userCode })
                        Log.d("FavoritesScreenViewModel", "Candidatos favoritados carregados: ${favorites.size}")
                    }
                }
            } catch (e: Exception) {
                _error.value = "Erro ao carregar favoritos: ${e.message}"
                Log.e("FavoritesScreenViewModel", "Erro: ${e.message}")
            } finally {
                _isLoading.value = false
                Log.d("FavoritesScreenViewModel", "Carregamento concluído, isLoading: ${_isLoading.value}")
            }
        }
    }

    fun toggleFavorite(jobId: String) {
        viewModelScope.launch {
            val currentJobs = _favoriteJobs.value
            val job = currentJobs.find { it.jobId == jobId }
            if (job != null) {
                favoriteJobDao.delete(job)
                _favoriteJobs.value = currentJobs.filter { it.jobId != jobId }
                _favoriteJobDetails.value = _favoriteJobDetails.value.filter { it.id != jobId }
                Log.d("FavoritesScreenViewModel", "Vaga $jobId removida dos favoritos")
            }
        }
    }

    fun toggleFavoriteCandidate(userCode: String) {
        viewModelScope.launch {
            val currentCandidates = _favoriteCandidates.value
            val candidate = currentCandidates.find { it.userCode == userCode }
            if (candidate != null) {
                favoriteCandidateDao.delete(FavoriteCandidate(candidate.companyCode, userCode))
                _favoriteCandidates.value = currentCandidates.filter { it.userCode != userCode }
                _favoriteCandidateDetails.value = _favoriteCandidateDetails.value.filter { it.userCode.toString() != userCode }
                Log.d("FavoritesScreenViewModel", "Candidato $userCode removido dos favoritos")
            }
        }
    }

    private suspend fun loadFavoriteCandidateDetails(userCodes: List<String>) {
        if (userCodes.isEmpty()) {
            _favoriteCandidateDetails.value = emptyList()
            return
        }
        try {
            val userDao = database.userDao()
            val candidates = userCodes.mapNotNull { userCode ->
                userDao.searchUserById(userCode.toInt())
            }
            _favoriteCandidateDetails.value = candidates
        } catch (e: Exception) {
            _error.value = "Erro ao carregar detalhes dos candidatos: ${e.message}"
            Log.e("FavoritesScreenViewModel", "Exceção: ${e.message}")
        }
    }
}