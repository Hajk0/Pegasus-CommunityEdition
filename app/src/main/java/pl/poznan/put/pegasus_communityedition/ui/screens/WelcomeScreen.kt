package pl.poznan.put.pegasus_communityedition.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import pl.poznan.put.pegasus_communityedition.Screen
import pl.poznan.put.pegasus_communityedition.ui.sign_in.SignInState

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    state: SignInState,
    onSignInState: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onSignInState/*{ onLogInClick(navController) }*/) {
            Text(text = "Sign in")
        }
    }
}

fun onLogInClick(navController: NavHostController) {
    Firebase.analytics.logEvent("siemano", null)
    navController.navigate(Screen.HomeScreen.route)
}