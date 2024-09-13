package pl.poznan.put.pegasus_communityedition

sealed class Screen(val route: String, val id: Int) {
    object WelcomeScreen : Screen("welcome_screen", 0)
    object HomeScreen : Screen("home_screen", 1)
    object HistoryScreen : Screen("history_screen", 2)
    object ProfileScreen : Screen("profile_screen", 3)
    object StolenDataScreen : Screen("stolen_data_screen", 3)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}