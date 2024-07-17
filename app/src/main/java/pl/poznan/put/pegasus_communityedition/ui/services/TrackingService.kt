package pl.poznan.put.pegasus_communityedition.ui.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.poznan.put.pegasus_communityedition.R
import pl.poznan.put.pegasus_communityedition.ui.audio.AudioRecorder
import pl.poznan.put.pegasus_communityedition.ui.services.audio.AudioClient
import pl.poznan.put.pegasus_communityedition.ui.services.audio.DefaultAudioClient
import pl.poznan.put.pegasus_communityedition.ui.services.location.DefaultLocationClient
import pl.poznan.put.pegasus_communityedition.ui.services.location.LocationClient


class TrackingService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var audioClient: AudioClient
    private val recorder by lazy {
        AudioRecorder(applicationContext)
    }

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
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                Log.println(Log.DEBUG, "X(", "Siemano 2")
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        audioClient
            .getAudioSample(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { audioFile ->
                // TODO( Store in database )
                Log.println(Log.DEBUG, "XD", "Siemano")
            }
            .launchIn(serviceScope)

        startForeground(1,
            notification.build())
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