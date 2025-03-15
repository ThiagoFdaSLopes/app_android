package com.grupo.appandroid.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.model.User

class RegistrationViewModel: ViewModel() {

    // Estados para os campos da entidade (úteis para uma UI com Compose)
    val redirectRegister = mutableStateOf(false)
    val name = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var industry = mutableStateOf("")
    var document = mutableStateOf("")
    var location = mutableStateOf("")
    var skills = mutableStateOf("")
    var description = mutableStateOf("")
    var academyLevel = mutableStateOf("")
    var academyCourse = mutableStateOf("")
    var academyInstitution = mutableStateOf("")
    var academyLastYear = mutableStateOf("")

    // LiveData para armazenar o usuário criado ou selecionado
    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    // LiveData para Armazenar Company
    private val _company = MutableLiveData<Company>()
    var company: LiveData<Company> = _company

    // Função para criar um novo usuário com os dados dos estados
    fun createUser(): User {
        val newUser = User(
            userCode = 0, // O Room gerará o ID automaticamente
            name = name.value,
            phone = phone.value,
            email = email.value,
            password = password.value,
            document = document.value,
            location = location.value,
            skills = skills.value,
            description = if (description.value.isBlank()) "" else description.value,
            academyLevel = if (academyLevel.value.isBlank()) "" else academyLevel.value,
            academyCourse = if (academyCourse.value.isBlank()) "" else academyCourse.value,
            academyInstitution = if (academyInstitution.value.isBlank()) "" else academyInstitution.value,
            academyLastYear = if (academyLastYear.value.isBlank()) "" else academyLastYear.value,
        )

        _user.value = newUser

        return newUser
    }

    fun createCompany(): Company {
        val newCompany = Company(
            companyCode = 0, // O Room gerará o ID automaticamente
            companyName = name.value,
            phone = phone.value,
            email = email.value,
            password = password.value,
            document = document.value,
            location = location.value,
            industry = industry.value,
            description = if (description.value.isBlank()) null else description.value
        )
        _company.value = newCompany

        return newCompany
    }

}