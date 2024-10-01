package pl.poznan.put.pegasus_communityedition.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import pl.poznan.put.pegasus_communityedition.R
import pl.poznan.put.pegasus_communityedition.Screen
import pl.poznan.put.pegasus_communityedition.ui.services.TrackingService
import pl.poznan.put.pegasus_communityedition.ui.sign_in.SignInState

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    state: SignInState,
    onSignInState: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Show sign-in error toast, if any
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
        // Main Column to center the content vertically
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            // Google Icon
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .padding(16.dp)
                    .size(120.dp) // Adjust icon size
                    .clip(RoundedCornerShape(16.dp))
            )

            // Welcome Text
            Text(
                text = "Welcome to Pegasus!",
                style = MaterialTheme.typography.headlineLarge, // Adjust font style
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

            // Sign-in Button
            Button(
                onClick = onSignInState,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.medium // Rounded corners for the button
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_icon), // Google icon on button
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp) // Size of the Google icon inside the button
                )
                Text(
                    text = "Sign in with Google",
                    modifier = Modifier.padding(start = 8.dp) // Spacing between icon and text
                )
            }
        }
    }
}

fun onLogInClick(navController: NavHostController) {
    Firebase.analytics.logEvent("siemano", null)
    navController.navigate(Screen.HomeScreen.route)
}
