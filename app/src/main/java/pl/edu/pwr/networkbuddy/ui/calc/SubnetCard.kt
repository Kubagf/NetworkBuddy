package pl.edu.pwr.networkbuddy.ui.calc

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
import pl.edu.pwr.networkbuddy.data.SubnetResult

@Composable
fun SubnetCard(result: SubnetResult, index: Int) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Subnet ${index + 1}", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subnet Mask:", style = MaterialTheme.typography.bodyMedium)
                Text(text = "/${result.mask}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString("/${result.mask}"))
                        Toast.makeText(
                            context, "Subnet mask copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Network Address:", style = MaterialTheme.typography.bodyMedium)
                Text(text = result.networkAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.networkAddress))
                        Toast.makeText(
                            context, "Network address copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "First Host:", style = MaterialTheme.typography.bodyMedium)
                Text(text = result.firstHost,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.firstHost))
                        Toast.makeText(
                            context, "First host IP copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Last Host:", style = MaterialTheme.typography.bodyMedium)
                Text(text = result.lastHost,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.lastHost))
                        Toast.makeText(
                            context, "Last host IP copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Broadcast Address:", style = MaterialTheme.typography.bodyMedium)
                Text(text = result.broadcastAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.broadcastAddress))
                        Toast.makeText(
                            context, "Broadcast IP copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Number of Hosts:", style = MaterialTheme.typography.bodyMedium)
                Text(text = result.hosts.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(result.hosts.toString()))
                        Toast.makeText(
                            context, "Number of hosts copied to clipboard", Toast.LENGTH_SHORT
                        ).show()
                    })
            }
        }
    }
}