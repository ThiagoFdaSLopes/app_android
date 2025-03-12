package com.grupo.appandroid.database.dao

import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["userCode", "jobId"]
)
data class Favorite(
    val userCode: String,
    val jobId: String,
    val createdAt: Long = System.currentTimeMillis()
)