import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.FavoriteCandidate
import com.grupo.appandroid.database.dao.FavoriteCandidateDao
import com.grupo.appandroid.database.dao.FavoriteJob
import com.grupo.appandroid.database.dao.FavoriteJobDao
import com.grupo.appandroid.model.User
import com.grupo.appandroid.service.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

enum class UserType {
    USER, COMPANY
}

class FavoritesScreenViewModel(
    private val favoriteCandidateDao: FavoriteCandidateDao,
    private val favoriteJobDao: FavoriteJobDao,
    private val database: AppDatabase // Adicionar o banco para acessar UserDAO
) : ViewModel() {
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
    private val _favoriteCandidates = MutableStateFlow<List<FavoriteCandidate>>(emptyList())
    val favoriteCandidates: StateFlow<List<FavoriteCandidate>> = _favoriteCandidates

    private val _favoriteJobs = MutableStateFlow<List<FavoriteJob>>(emptyList())
    val favoriteJobs: StateFlow<List<FavoriteJob>> = _favoriteJobs

    private val _favoriteJobDetails = MutableStateFlow<List<Job>>(emptyList())
    val favoriteJobDetails: StateFlow<List<Job>> = _favoriteJobDetails

    private val _favoriteCandidateDetails = MutableStateFlow<List<User>>(emptyList())
    val favoriteCandidateDetails: StateFlow<List<User>> = _favoriteCandidateDetails // Novo estado para candidatos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFavorites(userType: UserType, code: String) {
        viewModelScope.launch {
            when (userType) {
                UserType.COMPANY -> {
                    favoriteCandidateDao.getFavoritesByCompany(code).collect { favorites ->
                        _favoriteCandidates.value = favorites
                        Log.d("FavoritesScreenViewModel", "Candidatos favoritados encontrados: ${favorites.size}, userCodes: ${favorites.map { it.userCode }}")
                        loadFavoriteCandidateDetails(favorites.map { it.userCode })
                    }
                }
                UserType.USER -> {
                    favoriteJobDao.getFavoritesByUser(code).collect { favorites ->
                        _favoriteJobs.value = favorites
                        Log.d("FavoritesScreenViewModel", "Favoritos encontrados: ${favorites.size}, jobIds: ${favorites.map { it.jobId }}")
                        loadFavoriteJobDetails(favorites.map { it.jobId })
                    }
                }
            }
        }
    }

    private suspend fun loadFavoriteJobDetails(jobIds: List<String>) {
        if (jobIds.isEmpty()) {
            Log.d("FavoritesScreenViewModel", "Nenhum jobId encontrado para carregar detalhes")
            _favoriteJobDetails.value = emptyList()
            _isLoading.value = false
            return
        }

        _isLoading.value = true
        _error.value = null
        Log.d("FavoritesScreenViewModel", "Iniciando carregamento de detalhes para jobIds: $jobIds")

        try {
            val allJobs = mutableListOf<Job>()
            var page = 1
            var hasMorePages = true
            val maxPages = 5

            while (hasMorePages && page <= maxPages) {
                Log.d("FavoritesScreenViewModel", "Buscando página $page")
                val response = RetrofitClient.jobService.searchJobs(
                    page = page,
                    resultsPerPage = 50
                )

                if (response.isSuccessful) {
                    response.body()?.let { jobResponse ->
                        val jobs = jobResponse.results ?: emptyList()
                        Log.d("FavoritesScreenViewModel", "Página $page retornou ${jobs.size} vagas")
                        allJobs.addAll(jobs)

                        val foundJobIds = allJobs.map { it.id }.toSet()
                        if (jobIds.all { it in foundJobIds } || jobs.isEmpty()) {
                            Log.d("FavoritesScreenViewModel", "Todas as vagas favoritadas encontradas ou sem mais resultados")
                            hasMorePages = false
                        } else {
                            page++
                        }
                    } ?: run {
                        hasMorePages = false
                    }
                } else {
                    _error.value = "Erro ao buscar vagas na página $page: ${response.errorBody()?.string()}"
                    hasMorePages = false
                }
            }

            val filteredJobs = allJobs.filter { it.id in jobIds }
            Log.d("FavoritesScreenViewModel", "Vagas filtradas: ${filteredJobs.size}, IDs: ${filteredJobs.map { it.id }}")
            _favoriteJobDetails.value = filteredJobs
        } catch (e: Exception) {
            _error.value = "Erro ao carregar detalhes das vagas: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun loadFavoriteCandidateDetails(userCodes: List<String>) {
        if (userCodes.isEmpty()) {
            Log.d("FavoritesScreenViewModel", "Nenhum userCode encontrado para carregar detalhes")
            _favoriteCandidateDetails.value = emptyList()
            _isLoading.value = false
            return
        }

        _isLoading.value = true
        _error.value = null
        Log.d("FavoritesScreenViewModel", "Iniciando carregamento de detalhes para userCodes: $userCodes")

        try {
            val userDao = database.userDao()
            val candidates = userCodes.mapNotNull { userCode ->
                userDao.searchUserById(userCode.toInt()) // Converter String para Int
            }
            Log.d("FavoritesScreenViewModel", "Candidatos carregados: ${candidates.size}, nomes: ${candidates.map { it.name }}")
            _favoriteCandidateDetails.value = candidates
        } catch (e: Exception) {
            _error.value = "Erro ao carregar detalhes dos candidatos: ${e.message}"
            Log.e("FavoritesScreenViewModel", "Exceção: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("FavoritesScreenViewModel", "Carregamento de candidatos concluído")
        }
    }

    fun toggleFavorite(jobId: String) {
        viewModelScope.launch {
            val currentJobs = _favoriteJobs.value
            val job = currentJobs.find { it.jobId == jobId }
            if (job != null) {
                favoriteJobDao.delete(FavoriteJob(job.userCode, jobId))
                _favoriteJobs.value = currentJobs.filter { it.jobId != jobId }
                _favoriteJobDetails.value = _favoriteJobDetails.value.filter { it.id != jobId }
            }
        }
    }
}