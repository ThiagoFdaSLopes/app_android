package com.grupo.appandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class Company(
    @PrimaryKey(autoGenerate = true) val companyCode: Long,
    val companyName: String,
    val phone: String,
    val email: String,
    val password: String,
    val document: String,
    val location: String,
    val industry: String,
    val description: String?
)