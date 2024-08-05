package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import pl.poznan.put.pegasus_communityedition.Screen

@Composable
fun StolenDataScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column {
        Text("Hello here it will be stolen data")
        Button(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
            Text("Go back")
        }
    }

}