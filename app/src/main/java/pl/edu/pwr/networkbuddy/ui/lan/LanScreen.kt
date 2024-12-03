package pl.edu.pwr.networkbuddy.ui.lan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.pwr.networkbuddy.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanScreen(mainViewModel: MainViewModel) {
    val devices by mainViewModel.devices.collectAsState()
    val isScanning by mainViewModel.isScanning.collectAsState()
    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LAN Scanner",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            mainViewModel.scanNetworkDevices()
                        }, enabled = !isScanning, modifier = Modifier
                            .padding(4.dp)
                            .weight(0.5f)
                    ) {
                        Text("Scan")
                    }
                }
                if (isScanning) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.95f)
                    )
                }
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(devices.size) { index ->
                    DeviceCard(devices[index])
                }
            }
        }
    }
}