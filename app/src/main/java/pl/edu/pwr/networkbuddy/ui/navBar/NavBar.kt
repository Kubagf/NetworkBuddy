package pl.edu.pwr.networkbuddy.ui.navBar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
    val items = listOf("Home", "Wireless", "LAN", "Tools", "Calculator")
    val selectedIcons = listOf(
        R.drawable.home_filled,
        R.drawable.wifi_find_filled,
        R.drawable.lan_filled,
        R.drawable.tools_filled,
        R.drawable.calc_filled
    )
    val unselectedIcons = listOf(
        R.drawable.home, R.drawable.wifi_find, R.drawable.lan, R.drawable.tools, R.drawable.calc
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                if (pagerState.currentPage == index) {
                    Icon(
                        painter = painterResource(id = selectedIcons[index]),
                        contentDescription = item
                    )
                } else {
                    Icon(
                        painter = painterResource(id = unselectedIcons[index]),
                        contentDescription = item
                    )
                }
            }, label = { Text(item) }, selected = pagerState.currentPage == index, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    NavBar(pagerState = rememberPagerState(pageCount = { 5 }))
}
