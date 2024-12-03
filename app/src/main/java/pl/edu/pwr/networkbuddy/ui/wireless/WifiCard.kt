package pl.edu.pwr.networkbuddy.ui.wireless

import android.net.wifi.ScanResult
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.edu.pwr.networkbuddy.R
import pl.edu.pwr.networkbuddy.util.calculateChannel

@Composable
fun WifiCard(result: ScanResult) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val signalStrengthIcon = when {
        result.level >= -50 -> R.drawable.baseline_network_wifi_4_bar_24
        result.level in -70..-51 -> R.drawable.baseline_network_wifi_3_bar_24
        result.level in -80..-71 -> R.drawable.baseline_network_wifi_2_bar_24
        else -> R.drawable.baseline_network_wifi_1_bar_24
    }

    val signalColor = when {
        result.level >= -50 -> Color(0xFF4CAF50)
        result.level in -70..-51 -> Color(0xFFFFEB3B)
        result.level in -80..-71 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = result.SSID.ifEmpty { "(Hidden SSID)" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.SSID.ifEmpty { "(Hidden SSID)" }))
                        Toast.makeText(
                            context, "SSID copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
                Icon(
                    painter = painterResource(id = signalStrengthIcon),
                    contentDescription = "Signal Strength",
                    tint = signalColor
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "BSSID: ${result.BSSID}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.BSSID))
                        Toast.makeText(
                            context, "BSSID copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
                Text(
                    text = "Freq: ${result.frequency} MHz",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Level: ${result.level} dBm", style = MaterialTheme.typography.bodySmall
                )

                val channel = calculateChannel(result.frequency)
                Text(
                    text = "Ch: ${if (channel == -1) "Unknown" else channel}",
                    style = MaterialTheme.typography.bodySmall
                )

                val channelWidthText = when (result.channelWidth) {
                    ScanResult.CHANNEL_WIDTH_20MHZ -> "20 MHz"
                    ScanResult.CHANNEL_WIDTH_40MHZ -> "40 MHz"
                    ScanResult.CHANNEL_WIDTH_80MHZ -> "80 MHz"
                    ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ -> "80+80 MHz"
                    ScanResult.CHANNEL_WIDTH_160MHZ -> "160 MHz"
                    ScanResult.CHANNEL_WIDTH_320MHZ -> "320 MHz"
                    else -> "Unknown"
                }

                Text(text = "Width: $channelWidthText", style = MaterialTheme.typography.bodySmall)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Capabilities:\n${result.capabilities}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(result.capabilities))
                            Toast
                                .makeText(
                                    context, "SSID copied to clipboard", Toast.LENGTH_SHORT
                                )
                                .show()
                        })
                if (result.capabilities.contains("WPA") || result.capabilities.contains("WEP")) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Secured Network",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
