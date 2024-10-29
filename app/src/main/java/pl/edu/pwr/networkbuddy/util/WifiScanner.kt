package pl.edu.pwr.networkbuddy.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.edu.pwr.networkbuddy.ui.wireless.WifiCard

@Composable
fun WifiScanner() {
    val context = LocalContext.current
    val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    //TODO: add caching of results
    val results = remember { mutableStateListOf<ScanResult>() }

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
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            kotlinx.coroutines.delay(30_000L)
        }
    }

    LazyColumn {
        items(results.size) { index ->
            WifiCard(results[index])
        }
    }
}

@Preview
@Composable
fun WifiScannerPreview() {
    WifiScanner()
}
