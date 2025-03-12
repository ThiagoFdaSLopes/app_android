package com.grupo.appandroid.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userCode = :userCode")
    fun getFavoritesByUser(userCode: String): Flow<List<Favorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userCode = :userCode AND jobId = :jobId)")
    suspend fun isFavorite(userCode: String, jobId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)
}