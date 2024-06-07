package pl.poznan.put.pegasus_communityedition.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import pl.poznan.put.pegasus_communityedition.Screen
import androidx.navigation.compose.composable
import pl.poznan.put.pegasus_communityedition.Greeting

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route,
    ) {
        composable(
            route = Screen.MainScreen.route
        ) {
            Greeting(name = "main")
        }
        composable(
            route = Screen.WelcomeScreen.route
        ) {
            Greeting(name = "Welcome")
        }
    }
}
