package pl.edu.pwr.networkbuddy.ui.navBar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import pl.edu.pwr.networkbuddy.R

@Composable
fun NavBar(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val items = listOf("Home", "Wireless", "Lan", "Tools")
    val selectedIcons = listOf(
        Icons.Filled.Home, R.drawable.baseline_network_wifi_4_bar_24, Icons.Filled.Star, Icons.Filled.Star
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Home, R.drawable.baseline_network_wifi_0_bar_24, Icons.Outlined.Star, Icons.Filled.Star
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                if (pagerState.currentPage == index) {
                    if (selectedIcons[index] is Int) {
                        Icon(
                            painter = painterResource(id = selectedIcons[index] as Int),
                            contentDescription = item
                        )
                    } else {
                        Icon(
                            imageVector = selectedIcons[index] as androidx.compose.ui.graphics.vector.ImageVector,
                            contentDescription = item
                        )
                    }
                } else {
                    if (unselectedIcons[index] is Int) {
                        Icon(
                            painter = painterResource(id = unselectedIcons[index] as Int),
                            contentDescription = item
                        )
                    } else {
                        Icon(
                            imageVector = unselectedIcons[index] as androidx.compose.ui.graphics.vector.ImageVector,
                            contentDescription = item
                        )
                    }
                }
            }, label = { Text(item) }, selected = pagerState.currentPage == index, onClick = {
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    NavBar(pagerState = rememberPagerState(pageCount = { 4 }))
}
