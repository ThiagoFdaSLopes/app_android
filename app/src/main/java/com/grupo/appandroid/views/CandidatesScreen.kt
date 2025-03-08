package com.grupo.appandroid.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.model.User
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.ui.theme.DarkBackground
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CandidatesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val isCompanyLogin = prefs.getString("loginType", null) == "company"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopHeader()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                val users = listOf(
                    User(
                        userCode = 1L,
                        name = "Carlos Eduardo",
                        phone = "11987654321",
                        email = "carlos@example.com",
                        password = "hashedPass123",
                        document = "123.456.789-00",
                        location = "São Paulo - SP",
                        skills = "Desenvolvedor FullStack",
                        description = "5 anos",
                        academyLevel = "Bacharelado",
                        academyCourse = "Ciência da Computação",
                        academyInstitution = "USP",
                        academyLastYear = "2018"
                    ),
                    User(
                        userCode = 2L,
                        name = "Ana Clara",
                        phone = "21976543210",
                        email = "ana.clara@example.com",
                        password = "hashedPass456",
                        document = "987.654.321-00",
                        location = "Rio de Janeiro - RJ",
                        skills = "Designer UX/UI",
                        description = "3 anos",
                        academyLevel = "Graduação",
                        academyCourse = "Design Gráfico",
                        academyInstitution = "UFRJ",
                        academyLastYear = "2020"
                    ),
                    User(
                        userCode = 3L,
                        name = "Pedro Almeida",
                        phone = "31988887777",
                        email = "pedro@example.com",
                        password = "hashedPass789",
                        document = "456.789.123-00",
                        location = "Belo Horizonte - MG",
                        skills = "Analista de Dados",
                        description = "4 anos",
                        academyLevel = "Mestrado",
                        academyCourse = "Estatística",
                        academyInstitution = "UFMG",
                        academyLastYear = "2019"
                    ),
                    User(
                        userCode = 4L,
                        name = "Mariana Silva",
                        phone = "41987651234",
                        email = "mariana.silva@example.com",
                        password = "hashedPass101",
                        document = "321.654.987-00",
                        location = "Curitiba - PR",
                        skills = "Engenheira de Software",
                        description = "6 anos",
                        academyLevel = "Bacharelado",
                        academyCourse = "Engenharia de Software",
                        academyInstitution = "UFPR",
                        academyLastYear = "2017"
                    ),
                    User(
                        userCode = 5L,
                        name = "João Victor",
                        phone = "51977778888",
                        email = "joao.victor@example.com",
                        password = "hashedPass202",
                        document = "789.123.456-00",
                        location = "Porto Alegre - RS",
                        skills = "Desenvolvedor Mobile",
                        description = "2 anos",
                        academyLevel = "Técnico",
                        academyCourse = "Desenvolvimento de Sistemas",
                        academyInstitution = "SENAC",
                        academyLastYear = "2021"
                    )
                )

                val companies = listOf(
                    Company(
                        companyCode = 1L,
                        companyName = "TechCorp",
                        phone = "11999999999",
                        email = "contact@techcorp.com",
                        password = "hashedPassCorp1",
                        document = "12.345.678/0001-99",
                        location = "São Paulo - SP",
                        industry = "Tecnologia",
                        description = "Desenvolvedor FullStack - Remoto - R$ 8.000"
                    ),
                    Company(
                        companyCode = 2L,
                        companyName = "InovaLabs",
                        phone = "21988884444",
                        email = "hr@inovalabs.com",
                        password = "hashedPassCorp2",
                        document = "98.765.432/0001-88",
                        location = "Rio de Janeiro - RJ",
                        industry = "Inovação",
                        description = "Designer UX/UI - Presencial - R$ 6.500"
                    ),
                    Company(
                        companyCode = 3L,
                        companyName = "DataMinds",
                        phone = "31977776666",
                        email = "jobs@dataminds.com",
                        password = "hashedPassCorp3",
                        document = "45.678.912/0001-77",
                        location = "Belo Horizonte - MG",
                        industry = "Análise de Dados",
                        description = "Cientista de Dados - Híbrido - R$ 10.000"
                    ),
                    Company(
                        companyCode = 4L,
                        companyName = "SoftPeak",
                        phone = "41988885555",
                        email = "careers@softpeak.com",
                        password = "hashedPassCorp4",
                        document = "23.456.789/0001-66",
                        location = "Curitiba - PR",
                        industry = "Software",
                        description = "Engenheiro de Software - Remoto - R$ 9.500"
                    ),
                    Company(
                        companyCode = 5L,
                        companyName = "MobileWave",
                        phone = "51966667777",
                        email = "recruit@mobilewave.com",
                        password = "hashedPassCorp5",
                        document = "78.912.345/0001-55",
                        location = "Porto Alegre - RS",
                        industry = "Desenvolvimento Mobile",
                        description = "Desenvolvedor Mobile - Híbrido - R$ 7.000"
                    )
                )

                if (isCompanyLogin) {
                    items(users) { user ->
                        CandidateCard(
                            name = user.name,
                            age = 0, // Idade fictícia não fornecida
                            location = user.location,
                            area = user.skills,
                            experienceTime = user.description ?: "Experiência não especificada",
                            isCompanyLogin = isCompanyLogin,
                            onClick = {
                                val encodedName = URLEncoder.encode(user.name, StandardCharsets.UTF_8.toString())
                                val encodedEmail = URLEncoder.encode(user.email, StandardCharsets.UTF_8.toString())
                                val encodedPhone = URLEncoder.encode(user.phone, StandardCharsets.UTF_8.toString())
                                val encodedLocation = URLEncoder.encode(user.location, StandardCharsets.UTF_8.toString())
                                val encodedSkills = URLEncoder.encode(user.skills, StandardCharsets.UTF_8.toString())
                                val encodedDescription = URLEncoder.encode(user.description ?: "", StandardCharsets.UTF_8.toString())

                                navController.navigate(
                                    "userDetail/${user.userCode}/$encodedName/$encodedEmail/$encodedPhone/$encodedLocation/$encodedSkills/$encodedDescription"
                                )
                            }
                        )
                    }
                } else {
                    items(companies) { company ->
                        val jobDetails = company.description?.split(" - ") ?: listOf("", "", "")
                        val title = jobDetails.getOrNull(0) ?: ""
                        val modality = jobDetails.getOrNull(1) ?: ""
                        val salary = jobDetails.getOrNull(2) ?: ""

                        JobCard(
                            title = title,
                            company = company.companyName,
                            location = company.location,
                            modality = modality,
                            salary = salary,
                            onClick = {
                                val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                                val encodedCompany = URLEncoder.encode(company.companyName, StandardCharsets.UTF_8.toString())
                                val encodedLocation = URLEncoder.encode(company.location, StandardCharsets.UTF_8.toString())
                                val encodedModality = URLEncoder.encode(modality, StandardCharsets.UTF_8.toString())
                                val encodedSalary = URLEncoder.encode(salary, StandardCharsets.UTF_8.toString())

                                navController.navigate(
                                    "jobDetail/$encodedTitle/$encodedCompany/$encodedLocation/$encodedModality/$encodedSalary"
                                )
                            }
                        )
                    }
                }
            }

            NavigationBar()
        }
    }
}