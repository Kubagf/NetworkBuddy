package pl.edu.pwr.networkbuddy.ui.wireless

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WirelessScreen(navController: NavHostController? = null) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var selectedBand by rememberSaveable { mutableStateOf("All") }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

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

    Scaffold(topBar = {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
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
                    selectedBand = when (selectedBand) {
                        "All" -> "2.4 GHz"
                        "2.4 GHz" -> "5 GHz"
                        "5 GHz" -> "6 GHz"
                        "6 GHz" -> "All"
                        else -> "All"
                    }
                }, modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(selectedBand)
            }
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (hasLocationPermission) {
                WifiScanner(selectedBand)
            } else {
                Text("App needs location permission, please grant it in settings")
            }
        }
    }
}

@Composable
fun WifiScanner(selectedBand: String) {
    val context = LocalContext.current
    val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val results = remember { mutableStateListOf<ScanResult>() }
    val filteredResults = remember { mutableStateListOf<ScanResult>() }

    LaunchedEffect(selectedBand) {
        filteredResults.clear()
        filteredResults.addAll(results.filter {
            when (selectedBand) {
                "2.4 GHz" -> it.frequency in 2400..2500
                "5 GHz" -> it.frequency in 4900..5900
                "6 GHz" -> it.frequency in 5925..7125
                else -> true
            }
        })
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (wifiManager.startScan()) {
                    results.clear()
                    results.addAll(wifiManager.scanResults)
                    results.sortByDescending { it.level }
                    filteredResults.clear()
                    filteredResults.addAll(results.filter {
                        when (selectedBand) {
                            "2.4 GHz" -> it.frequency in 2400..2500
                            "5 GHz" -> it.frequency in 4900..5900
                            "6 GHz" -> it.frequency in 5925..7125
                            else -> true
                        }
                    })
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            kotlinx.coroutines.delay(30_000L)
        }
    }

    LazyColumn {
        items(filteredResults.size) { index ->
            WifiCard(filteredResults[index])
        }
    }
}