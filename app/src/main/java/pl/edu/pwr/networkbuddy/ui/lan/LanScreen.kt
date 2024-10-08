package pl.edu.pwr.networkbuddy.ui.lan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LanScreen() {
    Box(Modifier.fillMaxSize()) {
        Text("LAN", textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.Center))
    }
}