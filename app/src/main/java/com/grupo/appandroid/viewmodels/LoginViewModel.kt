package com.grupo.appandroid.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grupo.appandroid.model.User

class LoginViewModel: ViewModel() {

        // Estados para os campos da entidade (úteis para uma UI com Compose)
        val password = mutableStateOf("")
        var email = mutableStateOf("")

        // LiveData para armazenar o usuário criado ou selecionado
        // EXAMPLE
        // private val _user = MutableLiveData<User>()
        // var user: LiveData<User> = _user

        // Função para criar um novo usuário com os dados dos estados
        fun login() {
            // TODO
        }
}