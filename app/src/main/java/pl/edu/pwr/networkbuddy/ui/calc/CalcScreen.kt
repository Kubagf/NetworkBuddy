package pl.edu.pwr.networkbuddy.ui.calc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.edu.pwr.networkbuddy.R
import pl.edu.pwr.networkbuddy.data.SubnetResult
import pl.edu.pwr.networkbuddy.util.calculateSubnets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcScreen() {
    var usersList by remember { mutableStateOf(listOf("")) }
    var networkAddress by remember { mutableStateOf("192.168.1.0") }
    var prefix by remember { mutableStateOf("24") }
    var results by remember { mutableStateOf<List<SubnetResult>>(emptyList()) }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(title = {
            Text(
                text = "Subnet Calculator", style = MaterialTheme.typography.titleLarge
            )
        })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                item {
                    TextField(
                        value = networkAddress,
                        onValueChange = { networkAddress = it },
                        label = { Text("Network address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    TextField(
                        value = prefix,
                        onValueChange = { prefix = it },
                        label = { Text("Network prefix (e.g. 24)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                usersList.forEachIndexed { index, userCount ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = userCount,
                                onValueChange = { updatedCount ->
                                    val updatedList = usersList.toMutableList()
                                    updatedList[index] = updatedCount
                                    usersList = updatedList
                                },
                                label = { Text("Number of hosts in subnet ${index + 1}") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Button(
                                onClick = {
                                    val updatedList = usersList.toMutableList()
                                    if (index in updatedList.indices) {
                                        updatedList.removeAt(index)
                                        usersList = updatedList
                                    }
                                }, modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Icon(
                                    painterResource(R.drawable.remove),
                                    contentDescription = "Remove"
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val newList = usersList.toMutableList()
                            newList.add("")
                            usersList = newList
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Add Another Subnet")
                    }
                }

                item {
                    Button(
                        onClick = {
                            results = calculateSubnets(usersList, networkAddress, prefix)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Calculate Subnets")
                    }
                }

                results.forEachIndexed { index, result ->
                    item {
                        SubnetCard(result, index)
                    }
                }
            }
        }
    }
}
