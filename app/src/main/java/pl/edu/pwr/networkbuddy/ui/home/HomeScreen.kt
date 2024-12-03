package pl.edu.pwr.networkbuddy.ui.home

import android.net.DhcpInfo
import android.net.wifi.WifiInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.edu.pwr.networkbuddy.MainViewModel
import pl.edu.pwr.networkbuddy.R
import pl.edu.pwr.networkbuddy.ui.wireless.WifiCard
import pl.edu.pwr.networkbuddy.util.intIpToStringLE
import pl.edu.pwr.networkbuddy.util.prefixToIp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val wifiInfo by mainViewModel.wifiInfo
    val dhcpInfo by mainViewModel.dhcpInfo
    val results by mainViewModel.wifiResults.collectAsState()
    val currentWifi = wifiInfo?.ssid?.let { ssid ->
        results.find { it.SSID == ssid.removeSurrounding("\"") }
    }
    val prefix = mainViewModel.prefix.value

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            mainViewModel.refreshNetworkInfo()
                            mainViewModel.scanWifi()
                        }, modifier = Modifier
                            .padding(4.dp)
                            .weight(0.5f)
                    ) {
                        Text("Refresh")
                    }
                }
            })
        })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (wifiInfo != null && dhcpInfo != null) {
                    if (currentWifi != null) {
                        item {
                            WifiCard(currentWifi)
                        }
                    }
                    item {
                        NetworkInfoContent(wifiInfo, dhcpInfo, prefix?.let { prefixToIp(it) })
                    }
                } else {
                    item {
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
                                text = "No Wifi connection found or missing permission",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Please connect to a Wifi network, ensure that location permission is granted and refresh",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                            Button(onClick = {
                                mainViewModel.refreshNetworkInfo()
                                mainViewModel.scanWifi()
                                mainViewModel.getPrefix()
                            }) {
                                Text("Refresh")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkInfoContent(
    wifiInfo: WifiInfo?, dhcpInfo: DhcpInfo?, subnet: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wifiInfo != null && dhcpInfo != null) {
            InfoRow("SSID hidden?", wifiInfo.hiddenSSID.toString())
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                InfoRow("Wifi standard", wifiInfo.wifiStandard.toString())
            }
            InfoRow("IP Address", intIpToStringLE(dhcpInfo.ipAddress))
            InfoRow("DHCP lease duration", "${dhcpInfo.leaseDuration.div(3600)} hours")
            InfoRow("Gateway", intIpToStringLE(dhcpInfo.gateway))
            InfoRow("Subnet Mask", subnet.toString())
            InfoRow("DNS1", intIpToStringLE(dhcpInfo.dns1))
            InfoRow("DNS2", intIpToStringLE(dhcpInfo.dns2))
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                InfoRow("Link Speed", "${wifiInfo.linkSpeed} Mbps")
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                InfoRow("Rx Link Speed", "${wifiInfo.rxLinkSpeedMbps} Mbps")
                InfoRow("Tx Link Speed", "${wifiInfo.txLinkSpeedMbps} Mbps")
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                InfoRow(
                    "Max supported Rx Link Speed", "${wifiInfo.maxSupportedRxLinkSpeedMbps} Mbps"
                )
                InfoRow(
                    "Max supported Tx Link Speed", "${wifiInfo.maxSupportedTxLinkSpeedMbps} Mbps"
                )
            }
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
                InfoRow("MAC Address", wifiInfo.macAddress)
            }
        } else {
            Text("No network connection available", fontSize = 16.sp)
        }
    }
}