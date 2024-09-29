package pl.poznan.put.pegasus_communityedition.ui.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.poznan.put.pegasus_communityedition.R
import pl.poznan.put.pegasus_communityedition.ui.audio.AudioRecorder
import pl.poznan.put.pegasus_communityedition.ui.screens.viewmodels.HomeViewModel
import pl.poznan.put.pegasus_communityedition.ui.services.audio.AudioClient
import pl.poznan.put.pegasus_communityedition.ui.services.audio.DefaultAudioClient
import pl.poznan.put.pegasus_communityedition.ui.services.location.DefaultLocationClient
import pl.poznan.put.pegasus_communityedition.ui.services.location.LocationClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class TrackingService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var audioClient: AudioClient
    private val recorder by lazy {
        AudioRecorder(applicationContext)
    }
    private val firestore = Firebase.firestore

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        audioClient = DefaultAudioClient(
            applicationContext,
            recorder
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {
        val notification = NotificationCompat.Builder(this, "tracking_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Location: null")
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L) // co 10 sekund
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())
                storeLocationInFirestore(lat, long)
            }
            .launchIn(serviceScope)

        audioClient
            .getAudioSample(10000L) // co 10 sekund
            .catch { e -> e.printStackTrace() }
            .onEach { audioFile ->
                storeAudioInFirestore(audioFile)
            }
            .launchIn(serviceScope)

        startForeground(1,
            notification.build())
    }

    @SuppressLint("SimpleDateFormat")
    private fun storeLocationInFirestore(latitude: String, longitude: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val locationData = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to currentDate,
            "userEmail" to LoggedInUser.userEmail
        )

        firestore.collection("locations")
            .add(locationData)
            .addOnSuccessListener { documentReference ->
                Log.d("TrackingService", "Location stored with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("TrackingService", "Failed to store location data: ${e.message}")
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun storeAudioInFirestore(audioFile: File) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val storageReference = FirebaseStorage.getInstance().reference.child("audio/${audioFile.name}")
        val fileUri = Uri.fromFile(audioFile)

        storageReference.putFile(fileUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val audioData = hashMapOf(
                        "fileName" to audioFile.name,
                        "fileSize" to audioFile.length(),
                        "fileUrl" to uri.toString(),
                        "timestamp" to currentDate,
                        "userEmail" to LoggedInUser.userEmail
                    )
                    Log.d("TrackingService", "Audio file path: ${audioFile.absolutePath}, size: ${audioFile.length()}")


                    firestore.collection("audio")
                        .add(audioData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("TrackingService", "Audio metadata stored with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("TrackingService", "Failed to store audio metadata: ${e.message}")
                        }
                }
            }
            .addOnFailureListener {e ->
                Log.e("TrackingService", "Failed to upload audio file: ${e.message}")
            }
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    enum class Actions {
        START, STOP
    }
}