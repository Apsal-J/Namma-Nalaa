package com.example.nammanalaa.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nammanalaa.viewmodel.MainViewModel
import com.example.nammanalaa.viewmodel.ReportViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object ReportIssue : Screen("report_issue")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    reportViewModel: ReportViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController, mainViewModel) }
        composable(Screen.Signup.route) { SignupScreen(navController, mainViewModel) }
        composable(Screen.Dashboard.route) { DashboardScreen(navController, mainViewModel) }
        composable(Screen.ReportIssue.route) { ReportIssueScreen(navController, mainViewModel, reportViewModel) }
        composable(Screen.Profile.route) { ProfileScreen(navController, mainViewModel) }
    }
}
