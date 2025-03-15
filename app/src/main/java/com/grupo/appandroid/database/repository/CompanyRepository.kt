package com.grupo.appandroid.database.repository

import android.content.Context
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.model.User

class CompanyRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).companyDao()

    fun save(company: Company): Long {
        return db.save(company)
    }

    fun update(company: Company): Int {
        return db.update(company)
    }

    fun delete(company: Company): Int {
        return db.delete(company)
    }

    fun findAllCompany(): List<Company> {
        return db.findAllCompany()
    }

    fun searchCompanyById(id: Int): Company {
        return db.searchCompanyById(id)
    }

    // Nova função para login
    fun findCompanyByEmailAndPassword(email: String, password: String): Company? {
        return db.findCompanyByEmailAndPassword(email, password)
    }

    fun findByEmail(email: String): Company? {
        return db.findCompanyByEmail(email = email)
    }

}