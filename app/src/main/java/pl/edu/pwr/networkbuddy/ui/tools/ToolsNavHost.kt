package pl.edu.pwr.networkbuddy.ui.tools

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ToolsNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "toolsScreen"
    ) {
        composable("toolsScreen") { ToolsScreen() }
    }
}