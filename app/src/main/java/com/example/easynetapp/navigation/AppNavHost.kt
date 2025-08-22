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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.easynetapp.data.ClientViewModel
import com.example.easynetapp.ui.theme.Screens.home.ClientHomeScreen
import com.example.easynetapp.ui.theme.Screens.home.ProviderHomeScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    clientViewModel : ClientViewModel = viewModel()
) {
    val isLoggedIn = authViewModel.isUserLoggedIn()
    var startDest by remember { mutableStateOf("login") }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            authViewModel.getUserRole { role ->
                startDest = when (role) {
                    "client" -> "client_home"
                    "provider" -> "provider_home"
                    else -> "login"
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("client_home") { ClientHomeScreen(navController, clientViewModel) }
        composable("provider_home") { ProviderHomeScreen(navController, clientViewModel) }
    }
}


