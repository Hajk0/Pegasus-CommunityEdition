package pl.poznan.put.pegasus_communityedition.ui.data.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Note : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var content: String = ""
    var userName: String = ""
    var timestamp: RealmInstant = RealmInstant.now()
}