package pl.poznan.put.pegasus_communityedition.ui.screens.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note
import pl.poznan.put.pegasus_communityedition.ui.services.TrackingApp

class HomeViewModel(var userEmail: String) : ViewModel() {

    private val realm = TrackingApp.realm
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
        }
    }
}