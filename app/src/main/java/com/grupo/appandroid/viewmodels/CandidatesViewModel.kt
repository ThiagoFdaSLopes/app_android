package com.grupo.appandroid.viewmodels

import Job
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.Category
import com.grupo.appandroid.model.User
import com.grupo.appandroid.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
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

    init {
        // Carregamento inicial
        if (isCompanyLogin) {
            fetchUsers(1)
        } else {
            loadCategories()
            fetchJobs(1)
        }
    }

    // Funções de atualização de filtros
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

    // Funções de busca
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