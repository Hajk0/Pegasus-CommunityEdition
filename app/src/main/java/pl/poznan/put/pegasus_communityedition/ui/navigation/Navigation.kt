package pl.poznan.put.pegasus_communityedition.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import pl.poznan.put.pegasus_communityedition.Screen
import androidx.navigation.compose.composable
import pl.poznan.put.pegasus_communityedition.Greeting
import pl.poznan.put.pegasus_communityedition.ui.screens.HomeScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.StolenDataScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.WelcomeScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.WelcomeScreen.route
        ) {
            WelcomeScreen(navController = navController)
        }
        composable(
            route = Screen.StolenDataScreen.route
        ) {
            StolenDataScreen(navController = navController)
        }
    }
}
