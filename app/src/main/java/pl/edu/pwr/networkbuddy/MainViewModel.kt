package pl.edu.pwr.networkbuddy

import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.edu.pwr.networkbuddy.util.getMacAddress
import pl.edu.pwr.networkbuddy.util.incrementIp
import pl.edu.pwr.networkbuddy.util.subnetToNetworkAddress
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

class MainViewModel(context: Context) : ViewModel() {
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val _wifiInfo = mutableStateOf<WifiInfo?>(null)
    val wifiInfo get() = _wifiInfo

    private val _dhcpInfo = mutableStateOf<DhcpInfo?>(null)
    val dhcpInfo get() = _dhcpInfo

    private val _wifiResults = MutableStateFlow<List<ScanResult>>(emptyList())
    val wifiResults: StateFlow<List<ScanResult>> = _wifiResults

    private val _prefix = mutableStateOf<Short?>(null)
    val prefix get() = _prefix

    private val _devices = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val devices: StateFlow<List<Pair<String, String>>> = _devices

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning


    init {
        refreshNetworkInfo()
        scanWifi()
        getPrefix()
    }

    fun getPrefix() {
        try {
            val networkInterface = NetworkInterface.getNetworkInterfaces().asSequence()
                .find { it.isUp && !it.isLoopback && it.name.contains("wlan") }
            _prefix.value =
                networkInterface?.interfaceAddresses?.firstOrNull { it.address is Inet4Address }?.networkPrefixLength
        } catch (e: Exception) {
            _prefix.value = null
        }
    }

    fun refreshNetworkInfo() {
        try {
            val connectionInfo = wifiManager.connectionInfo
            val dhcp = wifiManager.dhcpInfo
            _wifiInfo.value =
                connectionInfo.takeIf { it.ssid != null && it.ssid != "<unknown ssid>" }
            _dhcpInfo.value = dhcp.takeIf { it.ipAddress != 0 }
        } catch (e: SecurityException) {
            _wifiInfo.value = null
            _dhcpInfo.value = null
        }
    }

    fun scanWifi() {
        try {
            if (wifiManager.startScan()) {
                val results = wifiManager.scanResults
                _wifiResults.value = (results.sortedByDescending { it.level })
            }
        } catch (e: SecurityException) {
            _wifiResults.value = emptyList()
        }
    }

    fun scanNetworkDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            _isScanning.emit(true)
            _devices.emit(emptyList())
            try {
                val networkInterface = NetworkInterface.getNetworkInterfaces().asSequence()
                    .find { it.isUp && !it.isLoopback && it.name.contains("wlan") }
                val addressInfo =
                    networkInterface?.interfaceAddresses?.firstOrNull { it.address is Inet4Address }
                val prefixLength = addressInfo?.networkPrefixLength
                val subnet = addressInfo?.address?.hostAddress
                if (subnet != null && prefixLength != null) {
                    val network = subnetToNetworkAddress(subnet, prefixLength)
                    val numberOfHosts = (1 shl (32 - prefixLength)) - 2
                    for (i in 1..numberOfHosts) {
                        val ipAddress = incrementIp(network, i)
                        try {
                            val inetAddress = InetAddress.getByName(ipAddress)
                            if (inetAddress.isReachable(200)) {
                                val macAddress = getMacAddress(inetAddress) ?: "Unknown MAC"
                                val currentDevices = _devices.value.toMutableList()
                                currentDevices.add(ipAddress to macAddress)
                                _devices.emit(currentDevices)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isScanning.emit(false)
            }
        }
    }
}
