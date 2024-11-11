package pl.edu.pwr.networkbuddy.ui.calc

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun CalcNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "calcScreen"
    ) {
        composable("calcScreen") { CalcScreen() }
    }
}