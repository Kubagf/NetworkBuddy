package pl.edu.pwr.networkbuddy.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Box(Modifier.fillMaxSize()) {
        Text(
            "Home", textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.Center)
        )
    }
}
