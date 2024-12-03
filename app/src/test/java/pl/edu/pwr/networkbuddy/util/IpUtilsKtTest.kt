package pl.edu.pwr.networkbuddy.util

import org.junit.Assert.assertEquals
import org.junit.Test

class IpUtilsKtTest {

    @Test
    fun prefixToIp() {
        assertEquals("255.255.255.0", prefixToIp(24))
        assertEquals("255.255.0.0", prefixToIp(16))
        assertEquals("255.0.0.0", prefixToIp(8))
        assertEquals("0.0.0.0", prefixToIp(0))
    }

    @Test
    fun intIpToStringLE() {
        assertEquals("192.168.1.1", intIpToStringLE(16885952))
        assertEquals("10.0.0.0", intIpToStringLE(10))
    }

    @Test
    fun intIpToStringBE() {
        assertEquals("192.168.1.1", intIpToStringBE(3232235777.toInt()))
        assertEquals("10.0.0.0", intIpToStringBE(167772160))
    }

    @Test
    fun stringIpToIntLE() {
        assertEquals(3232235777.toInt(), stringIpToIntLE("192.168.1.1"))
        assertEquals(167772160, stringIpToIntLE("10.0.0.0"))
    }

    @Test
    fun testCalculateSubnets() {
        val userInputList = listOf("10", "30", "50")
        val subnet = "192.168.1.0"
        val prefix = "24"
        val result = calculateSubnets(userInputList, subnet, prefix)
        assertEquals(3, result.size)
        assertEquals("192.168.1.0", result[0].networkAddress)
        assertEquals("192.168.1.1", result[0].firstHost)
        assertEquals("192.168.1.14", result[0].lastHost)
        assertEquals("192.168.1.15", result[0].broadcastAddress)
        assertEquals(28, result[0].mask)
        assertEquals(14, result[0].hosts)


        assertEquals("192.168.1.16", result[1].networkAddress)
        assertEquals("192.168.1.17", result[1].firstHost)
        assertEquals("192.168.1.46", result[1].lastHost)
        assertEquals("192.168.1.47", result[1].broadcastAddress)
        assertEquals(27, result[1].mask)
        assertEquals(30, result[1].hosts)

        assertEquals("192.168.1.48", result[2].networkAddress)
        assertEquals("192.168.1.49", result[2].firstHost)
        assertEquals("192.168.1.110", result[2].lastHost)
        assertEquals("192.168.1.111", result[2].broadcastAddress)
        assertEquals(26, result[2].mask)
        assertEquals(62, result[2].hosts)
    }

    @Test
    fun subnetToNetworkAddress() {
        assertEquals("192.168.1.0", subnetToNetworkAddress("192.168.1.45", 24))
        assertEquals("192.168.10.0", subnetToNetworkAddress("192.168.10.45", 24))
        assertEquals("10.0.0.0", subnetToNetworkAddress("10.0.15.200", 8))
    }

    @Test
    fun incrementIp() {
        assertEquals("192.168.1.2", incrementIp("192.168.1.1", 1))
        assertEquals("192.168.1.10", incrementIp("192.168.1.1", 9))
        assertEquals("192.168.2.0", incrementIp("192.168.1.255", 1))
    }
}