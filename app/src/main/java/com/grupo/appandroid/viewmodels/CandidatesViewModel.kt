package com.grupo.appandroid.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.FavoriteCandidate
import com.grupo.appandroid.model.Category
import com.grupo.appandroid.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CandidatesViewModel(
    private val database: AppDatabase
) : ViewModel() {

    var users by mutableStateOf<List<User>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var currentPage by mutableStateOf(1)
        private set
    var totalPages by mutableStateOf(1)
        private set
    var categories by mutableStateOf<List<Category>>(emptyList())
    var searchQuery by mutableStateOf("")
    var selectedLocation by mutableStateOf("")
    var selectedArea by mutableStateOf("")
    var resultsPerPage by mutableStateOf(20)
    var selectedCategory by mutableStateOf("")
    private var companyCode = ""

    var favoriteCandidates by mutableStateOf<Set<String>>(emptySet())
        private set

    private val userDao = database.userDao()
    private val favoriteCandidateDao = database.favoriteCandidateDao()

    init {
        fetchUsers(1)
    }

    fun setCompanyCode(code: String) {
        companyCode = code
        fetchUsers(1)
        loadFavoriteCandidates()
    }

    private fun loadFavoriteCandidates() {
        viewModelScope.launch {
            favoriteCandidateDao.getFavoritesByCompany(companyCode).collect { favoriteCandidatesList ->
                favoriteCandidates = favoriteCandidatesList.map { it.userCode }.toSet()
                Log.d("CandidatesViewModel", "Favorite candidates updated: $favoriteCandidates")
            }
        }
    }
    fun toggleFavoriteCandidate(userCode: String) {
        viewModelScope.launch {
            try {
                val isFavorite = favoriteCandidateDao.isFavorite(companyCode, userCode)
                if (isFavorite) {
                    favoriteCandidateDao.delete(FavoriteCandidate(companyCode, userCode))
                    favoriteCandidates = favoriteCandidates - userCode
                } else {
                    favoriteCandidateDao.insert(FavoriteCandidate(companyCode, userCode))
                    favoriteCandidates = favoriteCandidates + userCode
                }
                Log.d("CandidatesViewModel", "Toggled favorite for $userCode. New state: $favoriteCandidates")
            } catch (e: Exception) {
                error = "Error toggling favorite candidate: ${e.message}"
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun updateLocation(location: String) {
        selectedLocation = location
        fetchUsers(1)
    }

    fun updateArea(area: String) {
        selectedArea = area
        currentPage = 1
        fetchUsers(1)
    }

    fun updateResultsPerPage(results: Int) {
        resultsPerPage = results
        currentPage = 1
        fetchUsers(1)
    }

    fun nextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchUsers(currentPage)
        }
    }

    fun previousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchUsers(currentPage)
        }
    }

    fun updateCategory(category: String) {
        selectedCategory = category
        currentPage = 1
        fetchUsers(1)
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
}
