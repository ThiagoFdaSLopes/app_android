package com.grupo.appandroid.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grupo.appandroid.database.repository.CompanyRepository
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.User

class LoginViewModel : ViewModel() {
        private val _email = MutableLiveData<String>()
        val email: LiveData<String> = _email

        private val _password = MutableLiveData<String>()
        val password: LiveData<String> = _password

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        private val _errorMessage = MutableLiveData<String>()
        val errorMessage: LiveData<String> = _errorMessage

        private val _loginSuccess = MutableLiveData<LoginResult?>()
        val loginSuccess: MutableLiveData<LoginResult?> = _loginSuccess

        fun logout(context: Context) {
                val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                prefs.edit()
                        .clear()
                        .apply()

                _email.value = ""
                _password.value = ""
                _loginSuccess.value = null
                _errorMessage.value = ""
        }

        fun updateEmail(value: String) {
                _email.value = value
        }

        fun updatePassword(value: String) {
                _password.value = value
        }

        fun login(
                userRepository: UserRepository,
                companyRepository: CompanyRepository
        ) {
                _isLoading.value = true
                _errorMessage.value = ""

                val emailValue = _email.value ?: ""
                val passwordValue = _password.value ?: ""

                // Basic validation
                if (emailValue.isEmpty() || passwordValue.isEmpty()) {
                        _errorMessage.value = "Por favor, preencha todos os campos"
                        _isLoading.value = false
                        return
                }

                // Check for user
                val user = userRepository.findUserByEmailAndPassword(emailValue, passwordValue)
                if (user != null) {
                        _loginSuccess.value = LoginResult(emailValue, "user")
                        _isLoading.value = false
                        return
                }

                // Check for company
                val company = companyRepository.findCompanyByEmailAndPassword(emailValue, passwordValue)
                if (company != null) {
                        _loginSuccess.value = LoginResult(emailValue, "company")
                        _isLoading.value = false
                        return
                }

                // Login failed
                _errorMessage.value = "Email ou senha inválidos"
                _isLoading.value = false
        }

        fun clearError() {
                _errorMessage.value = ""
        }
}

data class LoginResult(
        val email: String,
        val type: String
)
