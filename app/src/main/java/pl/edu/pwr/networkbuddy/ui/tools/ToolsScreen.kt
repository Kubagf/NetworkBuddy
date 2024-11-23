package pl.edu.pwr.networkbuddy.ui.tools

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen() {
    var host by rememberSaveable { mutableStateOf("") }
    var port by rememberSaveable { mutableStateOf("") }
    var output by rememberSaveable { mutableStateOf("") }
    var selectedIndex by rememberSaveable { mutableStateOf(-1) }
    val options = listOf("Ping", "Traceroute", "DNS lookup", "Port scan")

    LaunchedEffect(selectedIndex) {
        output = ""
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Text(
                text = "Tools", style = MaterialTheme.typography.titleLarge
            )
        })
    }, bottomBar = {
        Button(
            onClick = {
                when (selectedIndex) {
                    0 -> {
                        output = "Pinging $host..."
                        CoroutineScope(Dispatchers.IO).launch {
                            output = executePing(host)
                        }
                    }

                    1 -> {
                        output = "Tracerouting $host..."
                        CoroutineScope(Dispatchers.IO).launch {
                            output = executeTraceroute(host)
                        }
                    }

                    2 -> {
                        output = "Looking up DNS for $host..."
                        CoroutineScope(Dispatchers.IO).launch {
                            output = executeDnsLookup(host)
                        }
                    }

                    3 -> {
                        output = "Scanning port $port on $host..."
                        CoroutineScope(Dispatchers.IO).launch {
                            output = executePortScan(host, port.toIntOrNull() ?: 0)
                        }
                    }
                }
            },
            enabled = host.isNotEmpty() && selectedIndex >= 0,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Execute")
        }
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex
                        ) {
                            Text(label)
                        }
                    }
                }
                Row {
                    TextField(
                        value = host,
                        onValueChange = { host = it },
                        label = { Text("IP address / hostname") },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(2f)
                    )
                    if (selectedIndex == 3) {
                        TextField(
                            value = port,
                            onValueChange = { port = it },
                            label = { Text("Port") },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                ) {
                    item {
                        Text(
                            text = output,
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

suspend fun executePing(host: String): String {
    return executeCommand("ping -c 4 $host")
}

suspend fun executeTraceroute(host: String): String {
    return try {
        withContext(Dispatchers.IO) {
            val hops = mutableListOf<String>()
            var reachedDestination = false

            for (ttl in 1..30) {
                val command = "ping -c 1 -t $ttl $host"
                val result = executeCommand(command)
                val hopInfo = when {
                    result.contains("Time to live exceeded") -> {
                        val from = result.substringAfter("From ").substringBefore(":").trim()
                        "Hop $ttl: $from"
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
    } catch (e: Exception) {
        "Error executing traceroute"
    }
}

suspend fun executeDnsLookup(host: String): String {
    return try {
        val address = withContext(Dispatchers.IO) {
            InetAddress.getByName(host)
        }
        "Host: ${address.hostName}\nIP Address: ${address.hostAddress}"
    } catch (e: Exception) {
        "Error executing DNS lookup"
    }
}

suspend fun executePortScan(host: String, port: Int): String {
    return try {
        withContext(Dispatchers.IO) {
            val socket = java.net.Socket()
            socket.use {
                it.connect(java.net.InetSocketAddress(host, port), 1000)
                "Port $port on $host is open."
            }
        }
    } catch (e: Exception) {
        "Port $port on $host is closed or unreachable."
    }
}

suspend fun executeCommand(command: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val process = Runtime.getRuntime().exec(command)
            val output = process.inputStream.bufferedReader().use { it.readText() }
            process.waitFor()
            output.trim()
        } catch (e: Exception) {
            "Error executing command"
        }
    }
}
