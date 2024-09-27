package pl.poznan.put.pegasus_communityedition.ui.screens.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.mongodb.kbson.ObjectId
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note
import pl.poznan.put.pegasus_communityedition.ui.services.TrackingApp

class HomeViewModel(var userEmail: String) : ViewModel() {

    private val realm = TrackingApp.realm
    val firestore = Firebase.firestore
    var note: Note? = null

    private fun createSampleEntries() {
        viewModelScope.launch {
            realm.write {
                val note1 = Note().apply {
                    title = "Siemano"
                    content = "siemano kolano"
                    userName = "nie wiem"
                }
                val note2 = Note().apply {
                    title = "Siemano2"
                    content = "siemano kolano2"
                    userName = "nie wiemxd"
                }

                copyToRealm(note1, UpdatePolicy.ALL)
                copyToRealm(note2, UpdatePolicy.ALL)
            }
        }
    }



    var title = mutableStateOf("")
    var content = mutableStateOf("")
    var objectId = mutableStateOf("")
    var notes = mutableStateOf(emptyList<Note>())

    init {
        observeNotes()
        syncRealmToFirestore()
        syncFirestoreToRealm()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            getUserNotes().collectLatest { fetchedNotes ->
                notes.value = fetchedNotes
            }
        }
    }

    fun updateUserName(userEmail: String) {
        this.userEmail = userEmail
        observeNotes()
    }

    fun updateTitle(title: String) {
        this.title.value = title
    }

    fun updateContent(content: String) {
        this.content.value = content
    }

    fun updateObjectId(id: String) {
        this.objectId.value = id
    }

    /*fun getNotes() : Flow<List<Note>> {
        return realm
            .query<Note>()
            .asFlow()
            .map { result ->
                result.list.toList()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                emptyList()
            )
    }*/

    fun getUserNotes() : Flow<List<Note>> {
        return realm
            .query<Note>("userName == $0", userEmail)
            .asFlow()
            .map { result ->
                result.list.toList()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                emptyList()
            )
    }

    fun getNoteById(id: String) : Note? {
        note = realm
            .query<Note>(query = "_id == $0", ObjectId(hexString = id))
            .first()
            .find()
        return note
    }

    fun getLastNote() : Note? {
        return note
    }

    fun insertNote() {
        viewModelScope.launch(Dispatchers.IO) {
            if (title.value.isNotEmpty()) {
                val note = Note().apply {
                    title = this@HomeViewModel.title.value
                    content = this@HomeViewModel.content.value
                    userName = this@HomeViewModel.userEmail
                }
                try {
                    saveNoteToFirestore(note)
                    note.online = true
                } catch (e: Exception) {
                    note.online = false
                    Log.e("HomeViewModel", "Error: ${e.message}")
                }
                realm.write { copyToRealm(note) }
            }
        }
    }

    fun updateNote() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()) {
                val note = Note().apply {
                    _id = ObjectId(hexString = this@HomeViewModel.objectId.value)
                    title = this@HomeViewModel.title.value
                    content = this@HomeViewModel.content.value
                    userName = this@HomeViewModel.userEmail
                }

                try {
                    saveNoteToFirestore(note)
                    note.online = true
                } catch (e: Exception) {
                    note.online = false
                }
                realm.write {
                    val queriedNote = query<Note>(query = "_id == $0", note._id).first().find()
                    queriedNote?.title = note.title
                    queriedNote?.content = note.content
                }
            }
        }
    }

    fun deleteNote(notePar: Note) {
        viewModelScope.launch {
            val id = notePar._id
            realm.write {
                val note = query<Note>(query = "_id == $0", id).first().find()
                try {
                    note?.let { delete(it) }
                } catch (e: Exception) {
                    Log.d("HomeViewModel", "${e.message}")
                }
            }
            // TODO( Add deleting from firestore )
            // firestore.collection("notes").document(id.toHexString()).delete().await()
        }
    }

    private fun syncRealmToFirestore() {
        viewModelScope.launch {
            realm.query<Note>("userName == $0", userEmail)
                .asFlow()
                .collect { realmResults ->
                    realmResults.list.forEach { note ->
                        saveNoteToFirestore(note)
                    }
                }
        }
    }

    private suspend fun saveNoteToFirestore(note: Note) {
        //try {
            val noteData = mapOf(
                "title" to note.title,
                "content" to note.content,
                "userName" to note.userName,
                "timestamp" to note.timestamp.toString()
            )
            firestore.collection("notes")
                .document(note._id.toHexString())
                .set(noteData)
                .await()
        /*} *//*catch (e: Exception) {
            Log.e("HomeViewModel", "Error saving note to Firestore: ${e.message}")
            // TODO( Add bool field in local DB called online and set offline here )
        }*/
    }

    private fun syncFirestoreToRealm() {
        viewModelScope.launch {
            firestore.collection("notes")
                .whereEqualTo("userName", userEmail)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("HomeViewModel", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val note = document.toObject(Note::class.java)
                            note?._id = ObjectId(document.id)
                            insertOrUpdateNoteInRealm(note)
                        }
                    }
                }
        }
    }

    private fun insertOrUpdateNoteInRealm(note: Note?) {
        note?.let {
            viewModelScope.launch(Dispatchers.IO) {
                realm.write {
                    copyToRealm(it, UpdatePolicy.ALL)
                }
            }
        }
    }
}