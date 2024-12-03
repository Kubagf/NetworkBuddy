package pl.edu.pwr.networkbuddy.util

import android.net.wifi.ScanResult

fun filterResults(results: List<ScanResult>, selectedBand: String): List<ScanResult> {
    return when (selectedBand) {
        "All" -> results
        "2.4 GHz" -> results.filter { it.frequency in 2400..2500 }
        "5 GHz" -> results.filter { it.frequency in 4900..5900 }
        "6 GHz" -> results.filter { it.frequency in 5925..7125 }
        else -> results
    }
}

fun calculateChannel(frequency: Int): Int {
    return when (frequency) {
        in 2412..2472 -> (frequency - 2407) / 5
        2484 -> 14
        in 5180..5825 -> (frequency - 5000) / 5
        in 5955..7115 -> (frequency - 5950) / 5
        else -> -1
    }
}