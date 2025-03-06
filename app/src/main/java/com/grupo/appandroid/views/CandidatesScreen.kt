package com.grupo.appandroid.views

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
import com.grupo.appandroid.componentes.CandidateCard
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.components.TopHeader
import com.grupo.appandroid.ui.theme.DarkBackground

@Composable
fun CandidatesScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Header Composable
            TopHeader()

            // Scrollable list of Candidate Cards
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Take remaining space
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                val candidates = listOf(
                    CandidateData("Carlos Eduardo", 25, "São Paulo - SP", "Developer", "5 years"),
                    CandidateData("Carlos Eduardo", 25, "São Paulo - SP", "Developer", "5 years"),
                    CandidateData("Carlos Eduardo", 25, "São Paulo - SP", "Developer", "5 years"),
                    CandidateData("Carlos Eduardo", 25, "São Paulo - SP", "Developer", "5 years")
                )

                items(candidates) { candidate ->
                    CandidateCard(
                        name = candidate.name,
                        age = candidate.age,
                        location = candidate.location,
                        area = candidate.area,
                        experienceTime = candidate.experienceTime

                    )
                }
            }

            // Bottom Navigation Bar Composable
            NavigationBar()
        }
    }
}

data class CandidateData(
    val name: String,
    val age: Int,
    val location: String,
    val area: String,
    val experienceTime: String
)