package pl.poznan.put.pegasus_communityedition.ui.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.dynamic.DynamicMutableRealmObject
import io.realm.kotlin.dynamic.DynamicRealm
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.query.RealmScalarNullableQuery
import io.realm.kotlin.schema.RealmSchema
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note

class TrackingApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()

        val config = RealmConfiguration.Builder(schema = setOf(Note::class))
            .schemaVersion(2)
            .migration(AutomaticSchemaMigration { context: AutomaticSchemaMigration.MigrationContext ->
                val oldVersion = context.oldRealm.schemaVersion()
                val schema = context.newRealm.schema()
                if (oldVersion < 2) {

                }
            })
            .build()

        realm = Realm.open(config)
            /*configuration = RealmConfiguration.create(
                schema = setOf(
                    Note::class,
                )
            )
        )*/
        val channel = NotificationChannel(
            "tracking_channel",
            "Tracking Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}