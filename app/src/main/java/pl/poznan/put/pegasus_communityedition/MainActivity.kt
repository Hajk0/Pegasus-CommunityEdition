package pl.poznan.put.pegasus_communityedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pl.poznan.put.pegasus_communityedition.ui.audio.AudioPlayer
import pl.poznan.put.pegasus_communityedition.ui.audio.AudioRecorder
import pl.poznan.put.pegasus_communityedition.ui.navigation.NavBar
import pl.poznan.put.pegasus_communityedition.ui.theme.PegasusCommunityEditionTheme
import java.io.File
import android.Manifest
import android.os.Build

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    private val recorder by lazy {
        AudioRecorder(applicationContext)
    }
    private val player by lazy {
        AudioPlayer(applicationContext)
    }
    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionArray = mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionArray.add(Manifest.permission.FOREGROUND_SERVICE)
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionArray.add(Manifest.permission.POST_NOTIFICATIONS)
            permissionArray.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissionArray.add(Manifest.permission.FOREGROUND_SERVICE_MICROPHONE)
            permissionArray.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        }

        ActivityCompat.requestPermissions(
            this,
            permissionArray.toTypedArray(),
            0
        )
        setContent {
            PegasusCommunityEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    /*Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {*/
                        /*Button(onClick = {
                            File(cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }
                        }) {
                            Text(text = "Start recording")
                        }
                        Button(onClick = {
                            recorder.stop()
                        }) {
                            Text(text = "Stop recording")
                        }
                        Button(onClick = {
                            player.playFile(audioFile?: return@Button)
                        }) {
                            Text(text = "Play")
                        }
                        Button(onClick = {
                            player.stop()
                        }) {
                            Text(text = "Stop playing")
                        }
                        Button(onClick = {
                            Intent(applicationContext, TrackingService::class.java).also {
                                it.action = TrackingService.Actions.START.toString()
                                startService(it)
                            }
                        }) {
                            Text(text = "Start foreground service")
                        }
                        Button(onClick = {
                            Intent(applicationContext, TrackingService::class.java).also {
                                it.action = TrackingService.Actions.STOP.toString()
                                startService(it)
                            }
                        }) {
                            Text(text = "Stop foreground service")
                        }*/
                    NavBar(navController)
                    // }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PegasusCommunityEditionTheme {
        Greeting("Android")
    }
}