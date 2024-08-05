package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import pl.poznan.put.pegasus_communityedition.Screen

@Composable
fun WelcomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Button(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
        Text(text = "Log in")
    }
}
