package pl.edu.pwr.networkbuddy.ui.tools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen() {
    var query by rememberSaveable { mutableStateOf("") }
    var output by rememberSaveable { mutableStateOf("") }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Tools", style = MaterialTheme.typography.titleLarge
            )
        })
    }) { it ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                item {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("IP address / hostname") },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
                item {
                    RowButtons { selectedLabel ->
                        selectedOption = selectedLabel
                        output = "Loading..."
                    }
                }
                item {
                    Text(
                        text = output, modifier = Modifier.padding(16.dp)
                    )
                }
            }

        }
    }

    LaunchedEffect(selectedOption, query) {
        if (query.isNotBlank() && selectedOption != null) {
            output = when (selectedOption) {
                "Ping" -> executePing(query)
                "Traceroute" -> executeTraceroute(query)
                "DNS lookup" -> executeDnsLookup(query)
                else -> "Invalid option"
            }
        }
    }
}

@Composable
fun RowButtons(onClick: (String) -> Unit) {
    val options = listOf("Ping", "Traceroute", "DNS lookup")

    Column(Modifier.padding(horizontal = 16.dp)) {
        options.forEach { label ->
            Button(
                onClick = {
                    onClick(label)
                }, modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                Text(label)
            }
        }
    }
}

suspend fun executePing(host: String): String {
    return executeCommand("ping -c 4 $host")
}

suspend fun executeTraceroute(host: String): String {
    return withContext(Dispatchers.IO) {
        val hops = mutableListOf<String>()
        var reachedDestination = false

        for (ttl in 1..30) {
            val command = "ping -c 1 -t $ttl $host"
            val result = executeCommand(command)
            val hopInfo = when {
                result.contains("time=") -> {
                    val time = result.substringAfter("time=").substringBefore(" ms").trim()
                    "Hop $ttl: Reached destination at $host in $time ms"
                }

                result.contains("Time to live exceeded") -> {
                    val from = result.substringAfter("From ").substringBefore(":").trim()
                    val time = executeCommand("ping -c 1 $from").substringAfter("time=")
                        .substringBefore(" ms").trim()
                    "Hop $ttl: $from, Time: $time ms"
                }

                result.contains("from") -> {
                    val from = result.substringAfter("from ").substringBefore(":").trim()
                    val time = result.substringAfter("time=").substringBefore(" ms").trim()
                    "Hop $ttl: $from, Time: $time ms"
                }

                else -> "Hop $ttl: *"
            }
            hops.add(hopInfo)
            if (result.contains("time=")) {
                reachedDestination = true
                break
            }
        }
        if (!reachedDestination) {
            hops.add("Destination not reached within 30 hops")
        }
        hops.joinToString(separator = "\n")
    }
}

suspend fun executeDnsLookup(host: String): String {
    return try {
        val address = withContext(Dispatchers.IO) {
            InetAddress.getByName(host)
        }
        "Host: ${address.hostName}\nIP Address: ${address.hostAddress}"
    } catch (e: Exception) {
        "DNS lookup error: ${e.message}"
    }
}

suspend fun executeCommand(command: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            reader.close()
            process.waitFor()
            output.toString()
        } catch (e: Exception) {
            "Error executing command: ${e.message}"
        }
    }
}
