package pl.poznan.put.pegasus_communityedition

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object WelcomeScreen : Screen("welcome_screen")
    object StolenDataScreen : Screen("stolen_data_screen")
    object ProfileScreen : Screen("profile_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}