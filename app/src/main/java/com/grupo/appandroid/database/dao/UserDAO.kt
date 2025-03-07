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
}