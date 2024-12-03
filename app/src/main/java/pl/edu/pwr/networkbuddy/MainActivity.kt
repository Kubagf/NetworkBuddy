package pl.edu.pwr.networkbuddy

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import pl.edu.pwr.networkbuddy.ui.calc.CalcScreen
import pl.edu.pwr.networkbuddy.ui.home.HomeScreen
import pl.edu.pwr.networkbuddy.ui.lan.LanScreen
import pl.edu.pwr.networkbuddy.ui.navBar.NavBar
import pl.edu.pwr.networkbuddy.ui.permissionScreen.PermissionScreen
import pl.edu.pwr.networkbuddy.ui.theme.NetworkBuddyTheme
import pl.edu.pwr.networkbuddy.ui.tools.ToolsScreen
import pl.edu.pwr.networkbuddy.ui.wireless.WirelessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var hasLocationPermission by remember { mutableStateOf(false) }
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                hasLocationPermission = isGranted
            }
            LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    hasLocationPermission = true
                }
            }
            val mainViewModel = MainViewModel(context)
            val pagerState = rememberPagerState(pageCount = { 5 })
            NetworkBuddyTheme {
                if (!hasLocationPermission) {
                    PermissionScreen()
                } else {
                    Scaffold(contentWindowInsets = WindowInsets(4.dp),
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { NavBar(pagerState) }) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                            ) { page ->
                                when (page) {
                                    0 -> HomeScreen(mainViewModel)
                                    1 -> WirelessScreen(mainViewModel)
                                    2 -> LanScreen(mainViewModel)
                                    3 -> ToolsScreen()
                                    4 -> CalcScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}