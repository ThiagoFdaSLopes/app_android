package com.grupo.appandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo.appandroid.model.CandidateData
import com.grupo.appandroid.model.JobData
import com.grupo.appandroid.ui.theme.RegistrationAppTheme
import com.grupo.appandroid.views.CandidatesScreen
import com.grupo.appandroid.viewmodels.LoginViewModel
import com.grupo.appandroid.viewmodels.RegistrationViewModel
import com.grupo.appandroid.views.CandidateDetailScreen
import com.grupo.appandroid.views.JobDetailScreen
import com.grupo.appandroid.views.LoginScreen
import com.grupo.appandroid.views.RegistrationScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
                        CandidatesScreen(navController = navController)
                    }
                    composable("candidateDetail/{name}/{age}/{location}/{area}/{experience}") { backStackEntry ->
                        val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", StandardCharsets.UTF_8.toString())
                        val age = backStackEntry.arguments?.getString("age")?.toInt() ?: 0
                        val location = URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "", StandardCharsets.UTF_8.toString())
                        val area = URLDecoder.decode(backStackEntry.arguments?.getString("area") ?: "", StandardCharsets.UTF_8.toString())
                        val experience = URLDecoder.decode(backStackEntry.arguments?.getString("experience") ?: "", StandardCharsets.UTF_8.toString())
                        CandidateDetailScreen(
                            candidate = CandidateData(name, age, location, area, experience),
                            navController = navController
                        )
                    }
                    composable("jobDetail/{title}/{company}/{location}/{modality}/{salary}") { backStackEntry ->
                        val title = URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", StandardCharsets.UTF_8.toString())
                        val company = URLDecoder.decode(backStackEntry.arguments?.getString("company") ?: "", StandardCharsets.UTF_8.toString())
                        val location = URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "", StandardCharsets.UTF_8.toString())
                        val modality = URLDecoder.decode(backStackEntry.arguments?.getString("modality") ?: "", StandardCharsets.UTF_8.toString())
                        val salary = URLDecoder.decode(backStackEntry.arguments?.getString("salary") ?: "", StandardCharsets.UTF_8.toString())
                        JobDetailScreen(
                            job = JobData(title, company, location, modality, salary),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}