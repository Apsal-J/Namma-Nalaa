package com.example.nammanalaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.nammanalaa.service.FirebaseService
import com.example.nammanalaa.ui.AppNavigation
import com.example.nammanalaa.ui.theme.NammaNalaaTheme
import com.example.nammanalaa.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val firebaseService = FirebaseService()
        
        enableEdgeToEdge()
        setContent {
            NammaNalaaTheme {
                val navController = rememberNavController()
                
                val mainViewModel: MainViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(firebaseService) as T
                    }
                })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        firebaseService = firebaseService
                    )
                }
            }
        }
    }
}
