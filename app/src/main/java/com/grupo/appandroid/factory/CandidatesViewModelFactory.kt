package com.grupo.appandroid.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.viewmodels.CandidatesViewModel

class CandidatesViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidatesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidatesViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
