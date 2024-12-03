package pl.edu.pwr.networkbuddy.util

import android.net.wifi.ScanResult
import org.junit.Assert.assertEquals
import org.junit.Test

class WifiUtilsKtTest {

    @Test
    fun filterResultsAll() {
        val results = listOf(ScanResult().apply { frequency = 2412 },
            ScanResult().apply { frequency = 5200 },
            ScanResult().apply { frequency = 6000 })

        val filtered = filterResults(results, "All")
        assertEquals(3, filtered.size)
    }

    @Test
    fun filterResults2_4GHz() {
        val results = listOf(ScanResult().apply { frequency = 2412 },
            ScanResult().apply { frequency = 5200 },
            ScanResult().apply { frequency = 6000 })

        val filtered = filterResults(results, "2.4 GHz")
        assertEquals(1, filtered.size)
        assertEquals(2412, filtered[0].frequency)
    }

    @Test
    fun filterResults5GHz() {
        val results = listOf(ScanResult().apply { frequency = 2412 },
            ScanResult().apply { frequency = 5200 },
            ScanResult().apply { frequency = 6000 })

        val filtered = filterResults(results, "5 GHz")
        assertEquals(1, filtered.size)
        assertEquals(5200, filtered[0].frequency)
    }

    @Test
    fun filterResults6GHz() {
        val results = listOf(ScanResult().apply { frequency = 2412 },
            ScanResult().apply { frequency = 5200 },
            ScanResult().apply { frequency = 6000 })

        val filtered = filterResults(results, "6 GHz")
        assertEquals(1, filtered.size)
        assertEquals(6000, filtered[0].frequency)
    }

    @Test
    fun calculateChannel2_4GHz() {
        assertEquals(1, calculateChannel(2412))
        assertEquals(11, calculateChannel(2462))
        assertEquals(14, calculateChannel(2484))
    }

    @Test
    fun calculateChannel5GHz() {
        assertEquals(36, calculateChannel(5180))
        assertEquals(165, calculateChannel(5825))
    }

    @Test
    fun calculateChannel6GHz() {
        assertEquals(1, calculateChannel(5955))
        assertEquals(101, calculateChannel(6455))
    }

    @Test
    fun calculateChannelInvalidFrequency() {
        assertEquals(-1, calculateChannel(1000))
        assertEquals(-1, calculateChannel(8000))
    }
}