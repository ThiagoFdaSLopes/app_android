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
import com.grupo.appandroid.components.CandidateCard
import com.grupo.appandroid.components.JobCard
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.model.CandidateData
import com.grupo.appandroid.model.JobData
import com.grupo.appandroid.ui.theme.DarkBackground

@Composable
fun CandidatesScreen(modifier: Modifier = Modifier) {
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
                    // Mostrar candidatos para empresas
                    items(candidates) { candidate ->
                        CandidateCard(
                            name = candidate.name,
                            age = candidate.age,
                            location = candidate.location,
                            area = candidate.area,
                            experienceTime = candidate.experienceTime,
                            isCompanyLogin = isCompanyLogin
                        )
                    }
                } else {
                    // Mostrar vagas para usuários
                    items(vagas) { vaga ->
                        JobCard(
                            title = vaga.title,
                            company = vaga.company,
                            location = vaga.location,
                            modality = vaga.modality,
                            salary = vaga.salary
                        )
                    }
                }
            }

            NavigationBar()
        }
    }
}