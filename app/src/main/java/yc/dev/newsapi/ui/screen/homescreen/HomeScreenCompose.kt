package yc.dev.newsapi.ui.screen.homescreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import yc.dev.newsapi.ui.route.Route
import yc.dev.newsapi.ui.theme.NewsApiTheme

@Composable
fun HomeScreen(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { navController.navigate(Route.Xml.route) },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "XML")
            }
        }

        Button(
            onClick = { navController.navigate(Route.Compose.route) },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Compose")
            }
        }
    }
}

@Preview(name = "Light Mode", device = Devices.PIXEL_4_XL, showSystemUi = true, showBackground = true)
@Preview(
    name = "Dark Mode",
    device = Devices.PIXEL_4_XL,
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreview() {
    NewsApiTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            HomeScreen(navController = rememberNavController())
        }
    }
}