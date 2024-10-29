package pl.edu.pwr.networkbuddy.ui.lan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanScreen() {
    Scaffold(topBar = {
        TopAppBar(title = { Text("LAN") })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text("Local IP: ")
        }
    }
}