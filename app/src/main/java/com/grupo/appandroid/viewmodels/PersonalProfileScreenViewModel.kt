package com.grupo.appandroid.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.model.User
import kotlinx.coroutines.launch

class PersonalProfileScreenViewModel() : ViewModel() {

    // Estados para cada campo do modelo User
    var userCode = mutableStateOf<Long?>(0)
    var name = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var document = mutableStateOf("")
    var location = mutableStateOf("")
    var skills = mutableStateOf("")
    var description = mutableStateOf("")
    var academyLevel = mutableStateOf("")
    var academyCourse = mutableStateOf("")
    var academyInstitution = mutableStateOf("")
    var academyLastYear = mutableStateOf("")
    var showSuccess = mutableStateOf(false)

    // Inicializa os estados com os dados atuais do usuário
    fun setUserData(user: User) {
        userCode.value = user.userCode
        name.value = user.name
        phone.value = user.phone
        email.value = user.email
        password.value = user.password
        document.value = user.document
        location.value = user.location
        skills.value = user.skills
        description.value = user.description ?: ""
        academyLevel.value = user.academyLevel ?: ""
        academyCourse.value = user.academyCourse ?: ""
        academyInstitution.value = user.academyInstitution ?: ""
        academyLastYear.value = user.academyLastYear ?: ""
    }

    // Função para atualizar os dados do usuário no banco
    fun updateUser(): User {
        val updatedUser = User(
            userCode = userCode.value ?: 0L, // Caso o código não esteja definido, use 0L
            name = name.value,
            phone = phone.value,
            email = email.value,
            password = password.value,
            document = document.value,
            location = location.value,
            skills = skills.value,
            description = if (description.value.isBlank()) null else description.value,
            academyLevel = if (academyLevel.value.isBlank()) null else academyLevel.value,
            academyCourse = if (academyCourse.value.isBlank()) null else academyCourse.value,
            academyInstitution = if (academyInstitution.value.isBlank()) null else academyInstitution.value,
            academyLastYear = academyLastYear.value
        )

        return updatedUser
    }
}
