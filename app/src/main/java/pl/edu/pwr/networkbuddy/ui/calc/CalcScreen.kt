package pl.edu.pwr.networkbuddy.ui.calc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Calculator",
                style = MaterialTheme.typography.titleLarge,
            )
        })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

        }
    }
}