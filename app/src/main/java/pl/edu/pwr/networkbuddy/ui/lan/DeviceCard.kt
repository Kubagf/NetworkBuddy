package pl.edu.pwr.networkbuddy.ui.lan

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun DeviceCard(device: Pair<String, String>) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(device.first))
                        Toast
                            .makeText(
                                context, "IP address copied to clipboard", Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "IP address:", style = MaterialTheme.typography.bodyMedium)
                Text(text = device.first, style = MaterialTheme.typography.bodyMedium)
            }
            if (device.second !== "Unknown MAC") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(device.second))
                            Toast
                                .makeText(
                                    context, "MAC address copied to clipboard", Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Mac:", style = MaterialTheme.typography.bodyMedium)
                    Text(text = device.second, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}