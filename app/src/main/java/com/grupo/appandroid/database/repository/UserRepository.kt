package com.grupo.appandroid.database.repository

import android.content.Context
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.User

class UserRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).userDao()

    fun save(user: User): Long {
        return db.save(user)
    }

    fun update(user: User): Int {
        return db.update(user)
    }

    fun delete(user: User): Int {
        return db.delete(user)
    }

    fun findAllUsers(): List<User> {
        return db.findAllUsers()
    }

    fun searchUserById(id: Int): User {
        return db.searchUserById(id)
    }

}