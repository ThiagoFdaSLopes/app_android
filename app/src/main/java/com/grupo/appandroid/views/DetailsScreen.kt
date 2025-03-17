package com.grupo.appandroid.views

import JobApplicationModal
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appandroid.R
import com.grupo.appandroid.componentes.NavigationBar
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.factory.CandidatesViewModelFactory
import com.grupo.appandroid.model.User
import com.grupo.appandroid.utils.SessionManager
import com.grupo.appandroid.viewmodels.CandidatesViewModel

@Composable
fun UserDetailScreen(
    user: User,
    viewModel: CandidatesViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val database = AppDatabase.getDatabase(context)
    val favoriteCandidateDao = database.favoriteCandidateDao()

    val companyCode = sessionManager.getLoggedInEmail()?.let { email ->
        UserRepository(context).findUserByEmail(email)?.userCode.toString()
    } ?: ""

    val isFavorite by remember(viewModel.favoriteCandidates) {
        derivedStateOf { viewModel.favoriteCandidates.contains(user.userCode.toString()) }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1E21))
            .systemBarsPadding()
            .padding(top = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { viewModel.toggleFavoriteCandidate(user.userCode.toString()) },
                modifier = Modifier.size(48.dp)
            ) {
                val animatedSize by animateDpAsState(
                    targetValue = if (isFavorite) 32.dp else 24.dp,
                    animationSpec = tween(durationMillis = 200)
                )
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.heart_fill else R.drawable.icon_heart
                    ),
                    contentDescription = "Favoritar",
                    tint = if (isFavorite) Color.Red else Color.White,
                    modifier = Modifier
                        .size(animatedSize)
                        .padding(4.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                user.academyCourse?.let { course ->
                    Text(
                        text = course,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            InfoItem(icon = "📱", label = stringResource(id = R.string.phone), value = user.phone)
            InfoItem(icon = "📍", label = stringResource(id = R.string.location), value = user.location)
            InfoItem(icon = "🎓", label = stringResource(id = R.string.academy_course), value = user.academyLevel ?: "Não informado")
            user.academyInstitution?.let { institution ->
                InfoItem(icon = "🏛️", label = stringResource(id = R.string.academy_institution), value = institution)
            }
            user.academyLastYear?.let { year ->
                InfoItem(icon = "📅", label = stringResource(id = R.string.academy_year_complete), value = year)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.skills),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = user.skills,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            user.description?.let { description ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.description),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.toggleFavoriteCandidate(user.userCode.toString()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = if (isFavorite) stringResource(id = R.string.remove_favorite) else stringResource(id = R.string.add_favorite),
                    fontSize = 16.sp
                )
            }
        }

        NavigationBar(navController = navController)
    }
}

@Composable
fun JobDetailScreen(
    jobId: String,
    title: String,
    company: String,
    location: String,
    modality: String,
    description: String,
    navController: NavController,
    viewModel: CandidatesViewModel
) {
    var showApplicationModal by remember { mutableStateOf(false) }
    // Se necessário, use o SessionManager para acessar dados da sessão
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val loggedInEmail = sessionManager.getLoggedInEmail()
    // Você pode utilizar o loggedInEmail para personalizar a tela se necessário

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1E21))
            .systemBarsPadding()
            .padding(top = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = company,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            InfoItem(icon = "📍", label = stringResource(id = R.string.location), value = location)
            InfoItem(icon = "💼", label = stringResource(id = R.string.modality), value = modality)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.description),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showApplicationModal = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(stringResource(id = R.string.sent_candidature), color = Color.Black)
            }
        }

        NavigationBar(navController = navController)
    }

    JobApplicationModal(
        isVisible = showApplicationModal,
        onDismiss = { showApplicationModal = false }
    )
}

@Composable
private fun InfoItem(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label $value",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

