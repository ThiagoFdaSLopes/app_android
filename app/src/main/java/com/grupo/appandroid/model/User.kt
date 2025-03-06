package com.grupo.appandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val userCode: Long,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val document: String,
    val location: String,
    val skills: String,
    val description: String?,
    val academyLevel: String?,
    val academyCourse: String?,
    val academyInstitution: String?,
    val academyLastYear: String?,
)