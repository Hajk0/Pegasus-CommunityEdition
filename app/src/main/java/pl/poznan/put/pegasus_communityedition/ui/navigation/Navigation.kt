package pl.poznan.put.pegasus_communityedition.ui.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import pl.poznan.put.pegasus_communityedition.Screen
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import pl.poznan.put.pegasus_communityedition.ui.screens.DetailsScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.HomeScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.ProfileScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.StolenDataScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.WelcomeScreen
import pl.poznan.put.pegasus_communityedition.ui.screens.viewmodels.HomeViewModel
import pl.poznan.put.pegasus_communityedition.ui.screens.viewmodels.HomeViewModelFactory
import pl.poznan.put.pegasus_communityedition.ui.services.TrackingService
import pl.poznan.put.pegasus_communityedition.ui.sign_in.GoogleAuthUiClient
import pl.poznan.put.pegasus_communityedition.ui.sign_in.SignInViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    onSelectedItemIndexChange: (Int) -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    val coroutineScope = rememberCoroutineScope()
    val appContext = LocalContext.current.applicationContext
    val userEmail = googleAuthUiClient.getSignedInUser()?.userEmail
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(userEmail.toString())
    )
    val notes by homeViewModel.notes
    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            TopBar(
                ScreenComposable = {
                    HomeScreen(
                        navController = navController,
                        userData = googleAuthUiClient.getSignedInUser(),
                        onSignOut = {
                            coroutineScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    appContext,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Screen.WelcomeScreen.route)
                                onSelectedItemIndexChange(Screen.WelcomeScreen.id)
                            }
                        },
                        notes = notes,
                        title = homeViewModel.title.value,
                        content = homeViewModel.content.value,
                        objectId = homeViewModel.objectId.value,
                        onTitleChanged = { homeViewModel.updateTitle(title = it) },
                        onContentChanged = { homeViewModel.updateContent(content = it) },
                        onObjectIdChanged = { homeViewModel.updateObjectId(id = it) },
                        onInsertClicked = { homeViewModel.insertNote() },
                        onUpdateClicked = { homeViewModel.updateNote() },
                        onDelete = { note -> homeViewModel.deleteNote(note) },
                        onDetail = { note ->
                            navController.navigate(Screen.DetailsScreen.withArgs(note._id.toHexString()))
                            onSelectedItemIndexChange(Screen.DetailsScreen.id)
                        }
                    )
                }, title = "Home"
            )
        }
        composable(
            route = Screen.WelcomeScreen.route
        ) {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(Screen.HomeScreen.route)
                    onSelectedItemIndexChange(Screen.HomeScreen.id)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        appContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(Screen.HomeScreen.route)
                    onSelectedItemIndexChange(Screen.HomeScreen.id)
                    viewModel.resetState()
                    homeViewModel.updateUserName(googleAuthUiClient.getSignedInUser()?.userEmail.toString())
                }
            }

            TopBar(
                ScreenComposable = {
                    WelcomeScreen(
                        navController = navController,
                        state = state,
                        onSignInState = {
                            coroutineScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                            startService(appContext)
                        }
                    )
                },
                title = "Welcome",
            )
        }
        composable(
            route = Screen.StolenDataScreen.route
        ) {
            TopBar(
                ScreenComposable = {
                    StolenDataScreen(navController = navController)
                },
                title = "Stolen Data"
            )
        }
        composable(
            route = Screen.ProfileScreen.route
        ) {
            TopBar(
                ScreenComposable = {
                    ProfileScreen(
                        navController = navController,
                        userData = googleAuthUiClient.getSignedInUser(),
                        onSignOut = {
                            coroutineScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    appContext,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(Screen.WelcomeScreen.route)
                                onSelectedItemIndexChange(Screen.WelcomeScreen.id)
                            }
                            stopService(appContext)
                        }
                    )
                },
                title = "Profile"
            )
        }
        composable(
            route = Screen.DetailsScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "Default"
                }
            )
        ) { entry ->
            entry.arguments?.getString("id")?.let {
                TopBar(
                    ScreenComposable = {
                        DetailsScreen(
                            note = homeViewModel.getNoteById(it),
                            onSaveClicked = { return@DetailsScreen },// TODO ( implement this )
                            onDeleteClicked = { return@DetailsScreen }// TODO ( implement this )
                        )
                    },
                    title = "Note"
                )
            }
        }
    }
}

private fun startService(appContext: Context) {
    Intent(appContext, TrackingService::class.java).also {
        it.action = TrackingService.Actions.START.toString()
        appContext.startService(it)
    }
}

private fun stopService(appContext: Context) {
    Intent(appContext, TrackingService::class.java).also {
        it.action = TrackingService.Actions.STOP.toString()
        appContext.startService(it)
    }
}