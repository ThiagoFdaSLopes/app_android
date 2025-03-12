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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo.appandroid.model.User
import com.grupo.appandroid.ui.theme.RegistrationAppTheme
import com.grupo.appandroid.viewmodels.LoginViewModel
import com.grupo.appandroid.viewmodels.RegistrationViewModel
import com.grupo.appandroid.views.FavoritesScreen
import com.grupo.appandroid.views.HomeScreen
import com.grupo.appandroid.views.JobDetailScreen
import com.grupo.appandroid.views.LoginScreen
import com.grupo.appandroid.views.RegistrationScreen
import com.grupo.appandroid.views.UserDetailScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                        RegistrationScreen(
                            navController = navController,
                            viewModel = RegistrationViewModel()
                        )
                    }
                    composable(
                        route = "FavoritesScreen",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ){
                        FavoritesScreen(navController)
                    }
                    composable(
                        route = "home",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
//                        CandidatesScreen(navController = navController)
                        HomeScreen(navController)
                    }
                    composable(
                        "userDetail/{userCode}/{name}/{email}/{phone}/{location}/{skills}/{description}"
                    ) { backStackEntry ->
                        val userCode =
                            backStackEntry.arguments?.getString("userCode")?.toLongOrNull() ?: 0L
                        val name = URLDecoder.decode(
                            backStackEntry.arguments?.getString("name") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val email = URLDecoder.decode(
                            backStackEntry.arguments?.getString("email") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val phone = URLDecoder.decode(
                            backStackEntry.arguments?.getString("phone") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val location = URLDecoder.decode(
                            backStackEntry.arguments?.getString("location") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val skills = URLDecoder.decode(
                            backStackEntry.arguments?.getString("skills") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val description = URLDecoder.decode(
                            backStackEntry.arguments?.getString("description") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )


                        val user = User(
                            userCode = userCode,
                            name = name,
                            email = email,
                            phone = phone,
                            password = "", // Not needed for display
                            document = "", // Not needed for display
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
                            navController = navController
                        )
                    }
                    // Fix candidate details route
                    composable(
                        route = "candidateDetail/{name}/{age}/{location}/{role}/{experience}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("age") { type = NavType.StringType },
                            navArgument("location") { type = NavType.StringType },
                            navArgument("role") { type = NavType.StringType },
                            navArgument("experience") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name =
                            URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "")
                        val age = URLDecoder.decode(
                            (backStackEntry.arguments?.getString("age")?.toIntOrNull()
                                ?: 0).toString()
                        )
                        val location =
                            URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "")
                        val role =
                            URLDecoder.decode(backStackEntry.arguments?.getString("role") ?: "")
                        val experience = URLDecoder.decode(
                            backStackEntry.arguments?.getString("experience") ?: ""
                        )


                        // Create a User object with the required fields
                        val user = User(
                            userCode = 0L, // Default value
                            name = name,
                            email = "", // Not needed for display
                            phone = "",
                            password = "",
                            document = "",
                            location = location,
                            skills = role,
                            description = "Experience: $experience",
                            academyLevel = null,
                            academyCourse = null,
                            academyInstitution = null,
                            academyLastYear = null
                        )


                        UserDetailScreen(
                            user = user,
                            navController = navController
                        )
                    }
                    composable(
                        "jobDetail/{title}/{company}/{location}/{modality}/{salary}"
                    ) { backStackEntry ->
                        val title = URLDecoder.decode(
                            backStackEntry.arguments?.getString("title") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val company = URLDecoder.decode(
                            backStackEntry.arguments?.getString("company") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val location = URLDecoder.decode(
                            backStackEntry.arguments?.getString("location") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val modality = URLDecoder.decode(
                            backStackEntry.arguments?.getString("modality") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        val salary = URLDecoder.decode(
                            backStackEntry.arguments?.getString("salary") ?: "",
                            StandardCharsets.UTF_8.toString()
                        )


                        JobDetailScreen(
                            title = title,
                            company = company,
                            location = location,
                            modality = modality,
                            salary = salary,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
