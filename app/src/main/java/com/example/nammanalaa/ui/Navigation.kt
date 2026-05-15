package com.example.nammanalaa.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nammanalaa.service.FirebaseService
import com.example.nammanalaa.viewmodel.MainViewModel
import com.example.nammanalaa.viewmodel.ReportViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object WaterFeed : Screen("water_feed")
    object Maintenance : Screen("maintenance")
    object SiltAlert : Screen("silt_alert")
    object Profile : Screen("profile")
    object ReportIssue : Screen("report_issue")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    firebaseService: FirebaseService
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController, mainViewModel) }
        composable(Screen.Signup.route) { SignupScreen(navController, mainViewModel) }
        composable(Screen.Dashboard.route) { DashboardScreen(navController, mainViewModel) }
        composable(Screen.WaterFeed.route) { WaterFeedScreen(navController, mainViewModel) }
        composable(Screen.Maintenance.route) { MaintenanceTrackerScreen(navController, mainViewModel) }
        composable(Screen.SiltAlert.route) { SiltAlertScreen(navController, mainViewModel) }
        composable(Screen.Profile.route) { ProfileScreen(navController, mainViewModel) }
        composable(Screen.ReportIssue.route) {
            val reportViewModel: ReportViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return ReportViewModel(firebaseService) as T
                }
            })
            ReportIssueScreen(navController, mainViewModel, reportViewModel)
        }
    }
}
