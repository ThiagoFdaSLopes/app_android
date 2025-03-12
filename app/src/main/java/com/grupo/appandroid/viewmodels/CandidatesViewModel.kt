package com.grupo.appandroid.viewmodels

import Job
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.Favorite
import com.grupo.appandroid.database.dao.FavoriteCandidate
import com.grupo.appandroid.model.Category
import com.grupo.appandroid.model.User
import com.grupo.appandroid.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CandidatesViewModel(
    private val database: AppDatabase,
    private val isCompanyLogin: Boolean
) : ViewModel() {

    // Estados
    var users by mutableStateOf<List<User>>(emptyList())
        private set
    var jobs by mutableStateOf<List<Job>>(emptyList())
        private set
    var categories by mutableStateOf<List<Category>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var currentPage by mutableStateOf(1)
        private set
    var totalPages by mutableStateOf(1)
        private set
    var searchQuery by mutableStateOf("")
    var selectedLocation by mutableStateOf("")
    var selectedCategory by mutableStateOf("")
    var selectedArea by mutableStateOf("")
    var resultsPerPage by mutableStateOf(20)

    private val userDao = database.userDao()
    private val favoriteDao = database.favoriteDao()
    private val favoriteCandidateDao = database.favoriteCandidateDao() // Added
    private var userCode = "" // Represents companyCode when isCompanyLogin is true

    private var _favorites by mutableStateOf<Set<String>>(emptySet()) // For jobs
    var favorites: Set<String> by mutableStateOf(emptySet()) // For jobs
        private set

    private var _favoriteCandidates by mutableStateOf<Set<String>>(emptySet()) // For candidates
    var favoriteCandidates: Set<String> by mutableStateOf(emptySet()) // For candidates
        private set

    init {
        if (isCompanyLogin) {
            fetchUsers(1)
        } else {
            loadCategories()
            fetchJobs(1)
        }
    }

    fun setUserCode(code: String) {
        userCode = code
        if (isCompanyLogin) {
            loadFavoriteCandidates()
        } else {
            loadFavorites()
        }
    }

    private fun loadFavorites() { // For jobs (non-company login)
        viewModelScope.launch {
            try {
                favoriteDao.getFavoritesByUser(userCode).collectLatest { favorites ->
                    _favorites = favorites.map { it.jobId }.toSet()
                    this@CandidatesViewModel.favorites = _favorites
                }
            } catch (e: Exception) {
                error = "Error loading favorites: ${e.message}"
            }
        }
    }

    private fun loadFavoriteCandidates() { // For candidates (company login)
        viewModelScope.launch {
            try {
                favoriteCandidateDao.getFavoritesByCompany(userCode).collectLatest { favoriteCandidates ->
                    _favoriteCandidates = favoriteCandidates.map { it.userCode }.toSet()
                    this@CandidatesViewModel.favoriteCandidates = _favoriteCandidates
                }
            } catch (e: Exception) {
                error = "Error loading favorite candidates: ${e.message}"
            }
        }
    }

    fun toggleFavorite(id: String) { // For jobs or candidates based on login type
        if (isCompanyLogin) {
            toggleFavoriteCandidate(id)
        } else {
            toggleFavoriteJob(id)
        }
    }

    private fun toggleFavoriteJob(jobId: String) { // For jobs (non-company login)
        viewModelScope.launch {
            try {
                val isFavorite = favoriteDao.isFavorite(userCode, jobId)
                if (isFavorite) {
                    favoriteDao.delete(Favorite(userCode, jobId))
                    _favorites = _favorites - jobId
                } else {
                    favoriteDao.insert(Favorite(userCode, jobId))
                    _favorites = _favorites + jobId
                }
                favorites = _favorites
            } catch (e: Exception) {
                error = "Error toggling favorite job: ${e.message}"
            }
        }
    }

    private fun toggleFavoriteCandidate(userCode: String) { // For candidates (company login)
        viewModelScope.launch {
            try {
                val isFavorite = favoriteCandidateDao.isFavorite(this@CandidatesViewModel.userCode, userCode)
                if (isFavorite) {
                    favoriteCandidateDao.delete(FavoriteCandidate(this@CandidatesViewModel.userCode, userCode))
                    _favoriteCandidates = _favoriteCandidates - userCode
                } else {
                    favoriteCandidateDao.insert(FavoriteCandidate(this@CandidatesViewModel.userCode, userCode))
                    _favoriteCandidates = _favoriteCandidates + userCode
                }
                favoriteCandidates = _favoriteCandidates
            } catch (e: Exception) {
                error = "Error toggling favorite candidate: ${e.message}"
            }
        }
    }

    // Filter update functions (unchanged)
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun updateLocation(location: String) {
        selectedLocation = location
        if (isCompanyLogin) fetchUsers(1)
    }

    fun updateCategory(category: String) {
        selectedCategory = category
        currentPage = 1
        if (!isCompanyLogin) fetchJobs(1)
    }

    fun updateArea(area: String) {
        selectedArea = area
        currentPage = 1
        if (isCompanyLogin) fetchUsers(1)
    }

    fun updateResultsPerPage(results: Int) {
        resultsPerPage = results
        currentPage = 1
        if (isCompanyLogin) fetchUsers(1) else fetchJobs(1)
    }

    fun nextPage() {
        if (currentPage < totalPages) {
            currentPage++
            if (isCompanyLogin) fetchUsers(currentPage) else fetchJobs(currentPage)
        }
    }

    fun previousPage() {
        if (currentPage > 1) {
            currentPage--
            if (isCompanyLogin) fetchUsers(currentPage) else fetchJobs(currentPage)
        }
    }

    private fun fetchUsers(page: Int) {
        viewModelScope.launch {
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
    }

    private fun fetchJobs(page: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.jobService.searchJobs(
                    page = page,
                    category = selectedCategory.takeIf { it.isNotEmpty() },
                    resultsPerPage = resultsPerPage
                )
                if (response.isSuccessful) {
                    response.body()?.let { jobResponse ->
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
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.jobService.getCategories()
                if (response.isSuccessful) {
                    categories = response.body()?.results ?: emptyList()
                }
            } catch (e: Exception) {
                error = "Error fetching categories: ${e.message}"
            }
        }
    }
}