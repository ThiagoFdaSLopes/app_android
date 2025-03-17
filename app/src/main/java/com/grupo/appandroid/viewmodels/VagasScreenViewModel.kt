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
    private val database: AppDatabase,
    private val userCode: String
) : ViewModel() {
    private val favoriteJobDao = database.favoriteJobDao()

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

    var favorites by mutableStateOf<Set<String>>(emptySet())
        private set

    private val favoriteDao = database.favoriteJobDao()

    init {
        if (userCode.isNotEmpty()) {
            loadFavorites()
            Log.d("JobsViewModel", "UserCode initialized to: $userCode")
        } else {
            Log.e("JobsViewModel", "UserCode is empty!")
        }
        loadCategories()
        fetchJobs(1)
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            if (userCode.isNotEmpty()) {
                favoriteDao.getFavoritesByUser(userCode).collectLatest { favoritesList ->
                    favorites = favoritesList.map { it.jobId }.toSet()
                    Log.d("JobsViewModel", "Favorites loaded for user $userCode: $favorites")
                }
            } else {
                Log.e("JobsViewModel", "Cannot load favorites: userCode is empty")
            }
        }
    }
    fun toggleFavoriteJob(jobId: String) {
        viewModelScope.launch {
            val job = jobs.find { it.id == jobId } ?: return@launch
            val currentFavorites = favorites
            if (jobId in currentFavorites) {
                favoriteJobDao.delete(FavoriteJob(
                    userCode = userCode,
                    jobId = job.id,
                    title = job.title,
                    description = job.description,
                    companyName = job.company.display_name,
                    locationName = job.location.display_name,
                    contractTime = job.contract_time
                ))
                favorites = currentFavorites.filter { it != jobId }.toSet()
            } else {
                val favoriteJob = FavoriteJob(
                    userCode = userCode,
                    jobId = job.id,
                    title = job.title,
                    description = job.description,
                    companyName = job.company.display_name,
                    locationName = job.location.display_name,
                    contractTime = job.contract_time
                )
                favoriteJobDao.insert(favoriteJob)
                favorites = currentFavorites + jobId
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