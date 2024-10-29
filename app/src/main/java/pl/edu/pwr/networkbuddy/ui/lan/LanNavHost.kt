package pl.edu.pwr.networkbuddy.ui.lan

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun LanNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "lanScreen"
    ) {
        composable("lanScreen") { LanScreen() }
    }
}