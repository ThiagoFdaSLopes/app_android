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
import com.grupo.appandroid.ui.theme.RegistrationAppTheme
import com.grupo.appandroid.views.LoginScreen
import com.grupo.appandroid.views.RegistrationScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                        LoginScreen(navController)
                    }
                    composable(
                        route = "registration",
                        enterTransition = { fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        RegistrationScreen(navController)
                    }

                }
            }
        }
    }
}