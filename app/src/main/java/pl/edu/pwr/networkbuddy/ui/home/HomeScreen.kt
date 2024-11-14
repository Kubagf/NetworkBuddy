package pl.edu.pwr.networkbuddy.ui.home

import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val wifiInfo = rememberSaveable { mutableStateOf<WifiInfo?>(null) }
    val dhcpInfo = rememberSaveable { mutableStateOf<DhcpInfo?>(null) }

    LaunchedEffect(Unit) {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiInfo.value = wifiManager.connectionInfo
            dhcpInfo.value = wifiManager.dhcpInfo
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Text(
                text = "Home",
                style = MaterialTheme.typography.titleLarge,
            )
        })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NetworkInfoContent(wifiInfo.value, dhcpInfo.value)
        }
    }
}

@Composable
fun NetworkInfoContent(wifiInfo: WifiInfo?, dhcpInfo: DhcpInfo?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wifiInfo != null && dhcpInfo != null) {
            InfoRow("SSID", wifiInfo.ssid)
            HorizontalDivider()
            InfoRow("BSSID", wifiInfo.bssid)
            HorizontalDivider()
            InfoRow("Subnet Mask", intToIp(dhcpInfo.netmask))
            HorizontalDivider()
            InfoRow("Gateway", intToIp(dhcpInfo.gateway))
            HorizontalDivider()
            InfoRow("DNS1", intToIp(dhcpInfo.dns1))
            HorizontalDivider()
            InfoRow("DNS2", intToIp(dhcpInfo.dns2))
            InfoRow("IP Address", intToIp(dhcpInfo.ipAddress))
            HorizontalDivider()
            InfoRow("DHCP lease duration", intToIp(dhcpInfo.leaseDuration))
            HorizontalDivider()
            InfoRow("Frequency", "${wifiInfo.frequency} MHz")
            HorizontalDivider()
            InfoRow("Signal Strength", "${wifiInfo.rssi} dBm")
            HorizontalDivider()
            InfoRow("Link Speed", "${wifiInfo.linkSpeed} Mbps")
            HorizontalDivider()
            InfoRow("MAC Address", wifiInfo.macAddress)
            HorizontalDivider()
        } else {
            Text("No network connection available", fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun intToIp(ip: Int): String {
    return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
}
