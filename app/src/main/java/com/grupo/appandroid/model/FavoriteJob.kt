package com.grupo.appandroid.database.dao

import androidx.room.Entity

@Entity(
    tableName = "favoritesJobs",
    primaryKeys = ["userCode", "jobId"]
)
data class FavoriteJob(
    val userCode: String,
    val jobId: String,
    val title: String,
    val description: String,
    val companyName: String, // Usamos companyName para simplificar, já que Company é um objeto
    val locationName: String, // Usamos locationName para simplificar, já que Location é um objeto
    val contractTime: String?,
    val createdAt: Long = System.currentTimeMillis()
)