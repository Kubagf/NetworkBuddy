package pl.edu.pwr.networkbuddy.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface

suspend fun executeCommand(command: String): String {
    return try {
        withContext(Dispatchers.IO) {
            val process = Runtime.getRuntime().exec(command)
            val output = process.inputStream.bufferedReader().use { it.readText() }
            process.waitFor()
            output.trim()
        }
    } catch (e: Exception) {
        "Error executing command"
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
                val result = executeCommand("ping -c 1 -t $ttl $host")
                val hopInfo = when {
                    result.contains("Time to live exceeded") -> {
                        val from = result.substringAfter("From ").substringBefore(":").trim()
                        "Hop $ttl: $from"
                    }

                    result.contains("time=") -> {
                        val from = result.substringAfter("from ").substringBefore(":").trim()
                        "Hop $ttl: $from"
                    }

                    else -> "Hop $ttl: *"
                }
                hops.add(hopInfo)
                if (result.contains("time=")) {
                    hops.add("Destination reached in $ttl hops")
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

fun getMacAddress(inetAddress: InetAddress): String? {
    val networkInterface = NetworkInterface.getByInetAddress(inetAddress)
    return networkInterface?.hardwareAddress?.joinToString(":") { "%02X".format(it) }
}