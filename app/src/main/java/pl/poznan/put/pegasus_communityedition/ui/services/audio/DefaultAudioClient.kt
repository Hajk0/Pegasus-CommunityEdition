package pl.poznan.put.pegasus_communityedition.ui.services.audio

import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import pl.poznan.put.pegasus_communityedition.ui.audio.AudioRecorder
import pl.poznan.put.pegasus_communityedition.ui.services.hasMicrophonePermission
import java.io.File
import kotlin.concurrent.fixedRateTimer

class DefaultAudioClient (
    private val context: Context,
    private val recorder: AudioRecorder
): AudioClient {
    override fun getAudioSample(interval: Long): Flow<File> {
        return callbackFlow {
            if (!context.hasMicrophonePermission()) {
                throw AudioClient.AudioException("Missing microphone permission")
            }

            while (true) {

                var audioFile: File
                File(context.cacheDir, "audio_service_${System.currentTimeMillis()}.mp3").also {
                    recorder.start(it)
                    audioFile = it
                }

                delay(interval)
                recorder.stop()

                launch { send(audioFile) }

                delay(interval)
            /*val timer = fixedRateTimer("audioTimer", initialDelay = 0L, period = interval) {
                launch { send(audioFile) }
            }*/

            }

            awaitClose {
                // timer.cancel()
                recorder.stop()
            }
        }
    }
}