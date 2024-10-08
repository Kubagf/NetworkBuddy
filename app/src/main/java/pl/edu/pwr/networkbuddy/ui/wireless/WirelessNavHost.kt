package pl.edu.pwr.networkbuddy.ui.wireless

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun WirelessNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "wirelessScreen"
    ) {
        composable("wirelessScreen") { WirelessScreen(navController) }
    }
}