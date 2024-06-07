package pl.poznan.put.pegasus_communityedition

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object WelcomeScreen : Screen("welcome_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}