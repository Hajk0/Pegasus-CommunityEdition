package pl.poznan.put.pegasus_communityedition.ui.services.audio

import kotlinx.coroutines.flow.Flow
import java.io.File

interface AudioClient {
    fun getAudioSample(interval: Long): Flow<File>

    class AudioException(message: String): Exception()
}