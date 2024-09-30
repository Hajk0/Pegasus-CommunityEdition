package pl.poznan.put.pegasus_communityedition.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import pl.poznan.put.pegasus_communityedition.Screen
import pl.poznan.put.pegasus_communityedition.ui.sign_in.GoogleAuthUiClient

@Composable
fun NavBar(
    navController: NavHostController,
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appContext = LocalContext.current.applicationContext
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = appContext,
            oneTapClient = Identity.getSignInClient(appContext)
        )
    }
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val context = LocalContext.current

    val items = listOf(
        NavigationItem(
            title = Screen.WelcomeScreen.route,
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
            description = "Welcome",
        ),
        NavigationItem(
            title = Screen.HomeScreen.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            description = "Home",
        ),
        NavigationItem(
            title = Screen.DetailsScreen.route,
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.List,
            description = "History",
        ),
        NavigationItem(
            title = Screen.ProfileScreen.route,
            selectedIcon = Icons.Filled.AccountBox,
            unselectedIcon = Icons.Outlined.AccountBox,
            description = "Profile",
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            if (googleAuthUiClient.getSignedInUser() != null) {
                                if (index == Screen.WelcomeScreen.id) {
                                    Toast.makeText(
                                        appContext,
                                        "Sign out to see Welcome Page",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@NavigationBarItem
                                }
                                selectedItemIndex = index
                                navController.navigate(item.title)
                            } else {
                                Toast.makeText(
                                    appContext,
                                    "Sign in to see Home Page",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.description
                            )
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(
                navController = navController,
                onSelectedItemIndexChange = {
                    selectedItemIndex = it
                },
                googleAuthUiClient = googleAuthUiClient,
                darkTheme = darkTheme,
                onThemeChange = onThemeChange,
            )
        }
    }
}