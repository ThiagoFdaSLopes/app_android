package com.grupo.appandroid.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteJobDao {
    @Query("SELECT * FROM favoritesJobs WHERE userCode = :userCode")
    fun getFavoritesByUser(userCode: String): Flow<List<FavoriteJob>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritesJobs WHERE userCode = :userCode AND jobId = :jobId)")
    suspend fun isFavorite(userCode: String, jobId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteJob)

    @Delete
    suspend fun delete(favorite: FavoriteJob)
}