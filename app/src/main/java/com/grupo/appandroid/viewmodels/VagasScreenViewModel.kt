package com.grupo.appandroid.viewmodels

import Job
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.FavoriteJob
import com.grupo.appandroid.model.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.grupo.appandroid.service.RetrofitClient

class VagasScreenViewModel(
    private val database: AppDatabase
) : ViewModel() {

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
    var resultsPerPage by mutableStateOf(20)
    var selectedArea by mutableStateOf("")
    private var userCode = ""

    var favorites by mutableStateOf<Set<String>>(emptySet())
        private set

    private val favoriteDao = database.favoriteDao()

    init {
        loadCategories()
        fetchJobs(1)
    }

    fun setUserCode(code: String) {
        userCode = code
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoriteDao.getFavoritesByUser(userCode).collectLatest { favoritesList ->
                favorites = favoritesList.map { it.jobId }.toSet()
                Log.d("JobsViewModel", "Favorites loaded: $favorites")
            }
        }
    }

    fun toggleFavoriteJob(jobId: String) {
        viewModelScope.launch {
            try {
                val isCurrentlyFavorite = favorites.contains(jobId)
                if (isCurrentlyFavorite) {
                    favoriteDao.delete(FavoriteJob(userCode, jobId))
                    favorites = favorites - jobId
                } else {
                    favoriteDao.insert(FavoriteJob(userCode, jobId))
                    favorites = favorites + jobId
                }
            } catch (e: Exception) {
                error = "Error toggling favorite job: ${e.message}"
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun updateLocation(location: String) {
        selectedLocation = location
        fetchJobs(1)
    }

    fun updateCategory(category: String) {
        selectedCategory = category
        currentPage = 1
        fetchJobs(1)
    }

    fun updateResultsPerPage(results: Int) {
        resultsPerPage = results
        currentPage = 1
        fetchJobs(1)
    }

    fun updateArea(area: String) {
        selectedArea = area
        currentPage = 1
        fetchJobs(1)
    }

    fun nextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchJobs(currentPage)
        }
    }

    fun previousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchJobs(currentPage)
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
