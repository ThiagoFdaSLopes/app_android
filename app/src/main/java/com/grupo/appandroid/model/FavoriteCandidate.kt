package com.grupo.appandroid.database.dao

import androidx.room.Entity

@Entity(
    tableName = "favorite_candidates",
    primaryKeys = ["companyCode", "userCode"]
)
data class FavoriteCandidate(
    val companyCode: String,
    val userCode: String,
    val createdAt: Long = System.currentTimeMillis()
)