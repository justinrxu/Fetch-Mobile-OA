package com.example.fetchmobileoa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fetchmobileoa.ui.theme.FetchMobileOATheme
import com.example.fetchmobileoa.ui.views.MainView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            FetchMobileOATheme {
                NavHost(navController = navController, startDestination = "/mainView") {
                    composable(
                        route = "/mainView",
                        enterTransition = { slideInVertically() + fadeIn() }
                    ) {
                        MainView(mainViewModel = hiltViewModel())
                    }
                }
            }
        }
    }
}
