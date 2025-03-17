package com.grupo.appandroid.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveUserSession(email: String, loginType: String) {
        prefs.edit().apply {
            putString("loggedInEmail", email)
            putString("loginType", loginType) // "company" ou "user"
            apply()
        }
    }

    fun getLoggedInEmail(): String? = prefs.getString("loggedInEmail", null)
    fun isCompanyLogin(): Boolean = prefs.getString("loginType", null) == "company"

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
