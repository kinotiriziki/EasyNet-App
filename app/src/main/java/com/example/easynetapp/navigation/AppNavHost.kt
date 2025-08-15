package com.example.easynetapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easynetapp.ui.theme.Screens.login.LoginScreen
import com.example.easynetapp.ui.theme.Screens.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(), startDestination: String= Route_LOGIN){
    NavHost(navController=navController, startDestination = startDestination){
        composable(Route_LOGIN) { LoginScreen(navController) }
        composable(Route_REGISTER) { RegisterScreen(navController) }

    }
}