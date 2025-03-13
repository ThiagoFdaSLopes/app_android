package com.grupo.appandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.RegistrationAppTheme
import com.grupo.appandroid.viewmodels.CandidatesViewModel
import com.grupo.appandroid.views.CandidatesScreen
import com.grupo.appandroid.viewmodels.LoginViewModel
import com.grupo.appandroid.viewmodels.RegistrationViewModel
import com.grupo.appandroid.views.LoginScreen
import com.grupo.appandroid.views.RegistrationScreen
import com.grupo.appandroid.views.UserDetailScreen
import com.grupo.appandroid.views.JobDetailScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appandroid.database.repository.UserRepository
import com.grupo.appandroid.views.CandidatesViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(5000)
            keepSplashScreen = false
        }

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            RegistrationAppTheme {
                val navController = rememberNavController()
                val database = AppDatabase.getDatabase(this)
                val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val isCompanyLogin = prefs.getString("loginType", null) == "company"

                val candidatesViewModel: CandidatesViewModel = viewModel(
                    factory = CandidatesViewModelFactory(database, isCompanyLogin)
                )

                LaunchedEffect(Unit) {
                    val email = prefs.getString("loggedInEmail", null)
                    val userRepository = UserRepository(this@MainActivity)
                    val user = userRepository.findUserByEmail(email ?: "")
                    user?.userCode?.let { candidatesViewModel.setUserCode(it.toString()) }
                }

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable(
                        route = "login",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        LoginScreen(navController, loginViewModel = LoginViewModel())
                    }
                    composable(
                        route = "registration",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        RegistrationScreen(navController = navController, viewModel = RegistrationViewModel())
                    }
                    composable(
                        route = "home",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        CandidatesScreen(
                            navController = navController,
                            viewModel = candidatesViewModel // Passe o viewModel como parâmetro
                        )
                    }
                    composable(
                        "userDetail/{userCode}/{name}/{email}/{phone}/{location}/{skills}/{description}"
                    ) { backStackEntry ->
                        val userCode = backStackEntry.arguments?.getString("userCode")?.toLongOrNull() ?: 0L
                        val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", StandardCharsets.UTF_8.toString())
                        val email = URLDecoder.decode(backStackEntry.arguments?.getString("email") ?: "", StandardCharsets.UTF_8.toString())
                        val phone = URLDecoder.decode(backStackEntry.arguments?.getString("phone") ?: "", StandardCharsets.UTF_8.toString())
                        val location = URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "", StandardCharsets.UTF_8.toString())
                        val skills = URLDecoder.decode(backStackEntry.arguments?.getString("skills") ?: "", StandardCharsets.UTF_8.toString())
                        val description = URLDecoder.decode(backStackEntry.arguments?.getString("description") ?: "", StandardCharsets.UTF_8.toString())

                        val user = User(
                            userCode = userCode,
                            name = name,
                            email = email,
                            phone = phone,
                            password = "",
                            document = "",
                            location = location,
                            skills = skills,
                            description = description,
                            academyLevel = null,
                            academyCourse = null,
                            academyInstitution = null,
                            academyLastYear = null
                        )
                        UserDetailScreen(
                            user = user,
                            navController = navController,
                            viewModel = candidatesViewModel // Passe o viewModel
                        )
                    }
                    composable(
                        "jobDetail/{jobId}/{title}/{company}/{location}/{modality}/{description}",
                        arguments = listOf(
                            navArgument("jobId") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType },
                            navArgument("company") { type = NavType.StringType },
                            navArgument("location") { type = NavType.StringType },
                            navArgument("modality") { type = NavType.StringType },
                            navArgument("description") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val jobId = URLDecoder.decode(backStackEntry.arguments?.getString("jobId") ?: "", StandardCharsets.UTF_8.toString())
                        val title = URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", StandardCharsets.UTF_8.toString())
                        val company = URLDecoder.decode(backStackEntry.arguments?.getString("company") ?: "", StandardCharsets.UTF_8.toString())
                        val location = URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "", StandardCharsets.UTF_8.toString())
                        val modality = URLDecoder.decode(backStackEntry.arguments?.getString("modality") ?: "", StandardCharsets.UTF_8.toString())
                        val description = URLDecoder.decode(backStackEntry.arguments?.getString("description") ?: "", StandardCharsets.UTF_8.toString())

                        JobDetailScreen(
                            jobId = jobId,
                            title = title,
                            company = company,
                            location = location,
                            modality = modality,
                            description = description,
                            navController = navController,
                            viewModel = candidatesViewModel
                        )
                    }
                }
            }
        }
    }
}

