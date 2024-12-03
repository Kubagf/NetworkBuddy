package pl.edu.pwr.networkbuddy.ui.wireless

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import pl.edu.pwr.networkbuddy.MainViewModel
import pl.edu.pwr.networkbuddy.R
import pl.edu.pwr.networkbuddy.util.filterResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WirelessScreen(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var selectedBand by rememberSaveable { mutableStateOf("2.4 GHz") }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    val results by mainViewModel.wifiResults.collectAsState()
    val filteredResults = remember { mutableStateListOf<ScanResult>() }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            hasLocationPermission = true
        }
    }

    LaunchedEffect(selectedBand) {
        filteredResults.clear()
        filteredResults.addAll(filterResults(results, selectedBand))
    }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(30_000L)
            mainViewModel.scanWifi()
            filteredResults.clear()
            filteredResults.addAll(filterResults(results, selectedBand))
            Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wireless",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        mainViewModel.scanWifi()
                        filteredResults.clear()
                        filteredResults.addAll(filterResults(results, selectedBand))
                        Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
                    }, modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text("Scan")
                }
                Button(
                    onClick = {
                        selectedBand = when (selectedBand) {
                            "2.4 GHz" -> "5 GHz"
                            "5 GHz" -> "6 GHz"
                            "6 GHz" -> "2.4 GHz"
                            else -> "2.4 GHz"
                        }
                    }, modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(selectedBand)
                }
            }
        })
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {
            if (hasLocationPermission) {
                LazyColumn {
                    items(filteredResults.size) { index ->
                        WifiCard(filteredResults[index])
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painterResource(R.drawable.wifi_off),
                        contentDescription = "Wifi turned off",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Missing permission",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Please grant location permission and refresh",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = {
                        mainViewModel.scanWifi()
                    }) {
                        Text("Refresh")
                    }
                }
            }
        }
    }
}