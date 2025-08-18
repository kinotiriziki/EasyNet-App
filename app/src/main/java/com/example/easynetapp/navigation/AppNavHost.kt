package com.example.easynetapp.navigation

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easynetapp.data.AuthViewModel
import com.example.easynetapp.ui.theme.Screens.login.LoginScreen
import com.example.easynetapp.ui.theme.Screens.register.RegisterScreen
import androidx.compose.material3.Text
import com.example.easynetapp.ui.theme.Screens.home.HomeScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val startDest = if (authViewModel.isUserLoggedIn()) "home" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("home") { HomeScreen(navController,authViewModel) }
    }
}

