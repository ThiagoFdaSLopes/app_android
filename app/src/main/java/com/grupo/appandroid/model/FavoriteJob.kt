package com.grupo.appandroid.database.dao

import androidx.room.Entity

@Entity(
    tableName = "favoritesJobs",
    primaryKeys = ["userCode", "jobId"]
)
data class FavoriteJob(
    val userCode: String,
    val jobId: String,
    val createdAt: Long = System.currentTimeMillis()
)