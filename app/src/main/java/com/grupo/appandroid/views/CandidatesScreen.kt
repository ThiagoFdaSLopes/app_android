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
import com.grupo.appandroid.model.CandidateData
import com.grupo.appandroid.model.JobData
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
                // Lista de candidatos
                val candidates = listOf(
                    CandidateData("Carlos Eduardo", 25, "São Paulo - SP", "Developer", "5 years"),
                    CandidateData("Ana Silva", 30, "Rio de Janeiro - RJ", "Designer", "3 years"),
                    CandidateData("João Pedro", 28, "Belo Horizonte - MG", "Manager", "7 years"),
                    CandidateData("Maria Oliveira", 27, "Curitiba - PR", "Developer", "4 years")
                )

                // Lista de vagas
                val vagas = listOf(
                    JobData("Desenvolvedor FullStack", "TechCorp", "São Paulo - SP", "Remoto", "R$ 8.000"),
                    JobData("Designer UI/UX", "Designify", "Rio de Janeiro - RJ", "Híbrido", "R$ 6.500"),
                    JobData("Gerente de Projetos", "ManageIt", "Belo Horizonte - MG", "Presencial", "R$ 10.000"),
                    JobData("Analista de Dados", "DataPros", "Curitiba - PR", "Remoto", "R$ 7.200")
                )

                if (isCompanyLogin) {
                    items(candidates) { candidate ->
                        CandidateCard(
                            name = candidate.name,
                            age = candidate.age,
                            location = candidate.location,
                            area = candidate.area,
                            experienceTime = candidate.experienceTime,
                            isCompanyLogin = isCompanyLogin,
                            onClick = {
                                val encodedName = URLEncoder.encode(candidate.name, StandardCharsets.UTF_8.toString())
                                val encodedLocation = URLEncoder.encode(candidate.location, StandardCharsets.UTF_8.toString())
                                val encodedArea = URLEncoder.encode(candidate.area, StandardCharsets.UTF_8.toString())
                                val encodedExperienceTime = URLEncoder.encode(candidate.experienceTime, StandardCharsets.UTF_8.toString())

                                navController.navigate(
                                    "candidateDetail/$encodedName/${candidate.age}/$encodedLocation/$encodedArea/$encodedExperienceTime"
                                )
                            }
                        )
                    }
                } else {
                    items(vagas) { vaga ->
                        JobCard(
                            title = vaga.title,
                            company = vaga.company,
                            location = vaga.location,
                            modality = vaga.modality,
                            salary = vaga.salary,
                            onClick = {
                                val encodedTitle = URLEncoder.encode(vaga.title, StandardCharsets.UTF_8.toString())
                                val encodedCompany = URLEncoder.encode(vaga.company, StandardCharsets.UTF_8.toString())
                                val encodedLocation = URLEncoder.encode(vaga.location, StandardCharsets.UTF_8.toString())
                                val encodedModality = URLEncoder.encode(vaga.modality, StandardCharsets.UTF_8.toString())
                                val encodedSalary = URLEncoder.encode(vaga.salary, StandardCharsets.UTF_8.toString())

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