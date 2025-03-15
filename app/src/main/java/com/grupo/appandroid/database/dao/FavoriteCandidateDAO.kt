package com.grupo.appandroid.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCandidateDao {
    @Query("SELECT * FROM favorite_candidates WHERE companyCode = :companyCode")
    fun getFavoritesByCompany(companyCode: String): Flow<List<FavoriteCandidate>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_candidates WHERE companyCode = :companyCode AND userCode = :userCode)")
    suspend fun isFavorite(companyCode: String, userCode: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteCandidate)

    @Delete
    suspend fun delete(favorite: FavoriteCandidate)
}