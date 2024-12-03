package pl.edu.pwr.networkbuddy.data

data class SubnetResult(
    val mask: Int,
    val networkAddress: String,
    val firstHost: String,
    val lastHost: String,
    val broadcastAddress: String,
    val hosts: Int
)