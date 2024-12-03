package pl.edu.pwr.networkbuddy.util

import pl.edu.pwr.networkbuddy.data.SubnetResult
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

fun prefixToIp(prefix: Short): String {
    val mask = intArrayOf(0, 0, 0, 0)
    for (i in 0 until prefix) {
        mask[i / 8] = mask[i / 8] or (1 shl (7 - i % 8))
    }
    return mask.joinToString(".")
}

//Little Endian
fun intIpToStringLE(ip: Int): String {
    return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
}

//Big Endian
fun intIpToStringBE(ip: Int): String {
    return "${ip shr 24 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 8 and 0xFF}.${ip and 0xFF}"
}

fun stringIpToIntLE(ip: String): Int {
    val parts = ip.split(".")
    return parts[3].toInt() or (parts[2].toInt() shl 8) or (parts[1].toInt() shl 16) or (parts[0].toInt() shl 24)
}

fun calculateSubnets(
    userInputList: List<String>, subnet: String, prefix: String
): List<SubnetResult> {
    val subnetResults = mutableListOf<SubnetResult>()
    val startingIp = stringIpToIntLE(subnetToNetworkAddress(subnet, prefix.toShort()))
    var nextNetwork = startingIp

    userInputList.forEach { input ->
        val numUsers = input.toIntOrNull() ?: 0
        if (numUsers < 2) {
            return@forEach
        }
        val requiredHosts = numUsers + 2
        val subnetSize = 32 - ceil(log2(requiredHosts.toDouble())).toInt()
        val numHosts = (2.0.pow(32 - subnetSize).toInt()) - 2
        val networkAddress = intIpToStringBE(nextNetwork)
        val firstHost = intIpToStringBE(nextNetwork + 1)
        val lastHost = intIpToStringBE(nextNetwork + numHosts)
        val broadcastAddress = intIpToStringBE(nextNetwork + numHosts + 1)
        subnetResults.add(
            SubnetResult(
                mask = subnetSize,
                networkAddress = networkAddress,
                firstHost = firstHost,
                lastHost = lastHost,
                broadcastAddress = broadcastAddress,
                hosts = numHosts
            )
        )
        nextNetwork += numHosts + 2
    }
    return subnetResults
}

fun subnetToNetworkAddress(subnet: String, prefixLength: Short): String {
    val subnetBytes = subnet.split(".").map { it.toInt() }
    val mask = (0xFFFFFFFF.toInt() shl (32 - prefixLength))
    val networkBytes = subnetBytes.zip(IntRange(
        0, 3
    ).map { i -> (mask shr (24 - i * 8)) and 0xFF }) { b, m -> b and m }
    return networkBytes.joinToString(".")
}

fun incrementIp(ip: String, increment: Int): String {
    val parts = ip.split(".").map { it.toInt() }.toMutableList()
    var temp = increment
    for (i in 3 downTo 0) {
        val sum = parts[i] + temp
        parts[i] = sum % 256
        temp = sum / 256
    }
    return parts.joinToString(".")
}
