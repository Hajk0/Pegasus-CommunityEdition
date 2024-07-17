package pl.poznan.put.pegasus_communityedition.ui.services.location

import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}