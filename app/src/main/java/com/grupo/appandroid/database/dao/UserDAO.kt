package com.grupo.appandroid.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.grupo.appandroid.model.User

@Dao
interface UserDAO {

    @Insert
    fun save(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("SELECT * FROM user WHERE userCode = :id")
    fun searchUserById(id: Int): User

    @Query("SELECT * FROM user ORDER BY name ASC")
    fun findAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    fun findUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    fun findUserByEmail(email: String): User?

    @Insert
    fun insertAll(users: List<User>)

    @Query("SELECT * FROM user " +
            "WHERE (:area IS NULL OR academyCourse LIKE :area OR skills LIKE :area) " +
            "ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun findUsersPaginated(
        area: String?,
        limit: Int,
        offset: Int
    ): List<User>

    @Query("SELECT COUNT(*) FROM user " +
            "WHERE (:area IS NULL OR academyCourse LIKE :area OR skills LIKE :area)")
    fun countUsersByArea(area: String?): Int


}