package com.grupo.appandroid.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.grupo.appandroid.model.Company

@Dao
interface CompanyDAO {

    @Insert
    fun save(company: Company): Long

    @Update
    fun update(company: Company): Int

    @Delete
    fun delete(company: Company): Int

    @Query("SELECT * FROM company WHERE companyCode  = :id")
    fun searchCompanyById(id: Int): Company

    @Query("SELECT * FROM company ORDER BY companyName ASC")
    fun findAllCompany(): List<Company>
}