package com.grupo.appandroid.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.grupo.appandroid.model.Company

class CompanyProfileScreenViewModel() : ViewModel() {

    // Estados para cada campo do modelo Company
    var companyCode = mutableStateOf<Long?>(0)
    var companyName = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var document = mutableStateOf("")
    var location = mutableStateOf("")
    var description = mutableStateOf("")
    var industry = mutableStateOf("")
    var showSuccess = mutableStateOf(false)

    // Inicializa os estados com os dados atuais da empresa
    fun setCompanyDate(company: Company) {
        companyCode.value = company.companyCode
        companyName.value = company.companyName
        phone.value = company.phone
        email.value = company.email
        password.value = company.password
        document.value = company.document
        location.value = company.location
        description.value = company.description ?: ""
        industry.value = company.industry
    }

    // Função para atualizar os dados da empresa no banco
    fun updateCompany(): Company {
        val company = Company(
            companyCode = companyCode.value ?: 0L, // Caso o código não esteja definido, use 0L
            companyName = companyName.value,
            phone = phone.value,
            email = email.value,
            password = password.value,
            document = document.value,
            location = location.value,
            industry = industry.value,
            description = if (description.value.isBlank()) "" else description.value,
        )

        return company
    }
}
