package com.grupo.appandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: String, // Can be either vacancy ID or user ID
    val itemType: String, // "VACANCY" or "USER"
    val userEmail: String // Email of the person who favorited
)