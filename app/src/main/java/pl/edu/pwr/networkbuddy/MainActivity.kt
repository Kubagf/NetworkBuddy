package pl.edu.pwr.networkbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import pl.edu.pwr.networkbuddy.ui.home.HomeNavHost
import pl.edu.pwr.networkbuddy.ui.lan.LanNavHost
import pl.edu.pwr.networkbuddy.ui.navBar.NavBar
import pl.edu.pwr.networkbuddy.ui.theme.NetworkBuddyTheme
import pl.edu.pwr.networkbuddy.ui.wireless.WirelessNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetworkBuddyTheme {
                val pagerState = rememberPagerState(pageCount = { 3 })
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { NavBar(pagerState) }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
                            when (page) {
                                0 -> HomeNavHost()
                                1 -> WirelessNavHost()
                                2 -> LanNavHost()
                            }
                        }
                    }
                }
            }
        }
    }
}