package pl.edu.pwr.networkbuddy.ui.permissionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.edu.pwr.networkbuddy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen() {
    Scaffold(modifier = Modifier.fillMaxSize(), contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Text(
                text = "Network Buddy",
                style = MaterialTheme.typography.titleLarge,
            )
        })
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(R.drawable.wifi_off),
                    contentDescription = "No location permission",
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "This app needs location permission to function properly!",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Please grant it in settings and restart the app.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}