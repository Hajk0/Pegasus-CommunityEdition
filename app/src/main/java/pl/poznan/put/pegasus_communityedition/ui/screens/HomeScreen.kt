package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pl.poznan.put.pegasus_communityedition.Screen
import pl.poznan.put.pegasus_communityedition.ui.components.NotesList
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note
import pl.poznan.put.pegasus_communityedition.ui.sign_in.UserData

@Composable
fun HomeScreen(
    navController: NavHostController,
    userData: UserData?,
    onSignOut: () -> Unit,
    notes: List<Note>,
    title: String,
    content: String,
    objectId: String,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onObjectIdChanged: (String) -> Unit,
    onInsertClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
    onDelete: (Note) -> Unit,
    onDetail: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*TextField(
            modifier = Modifier.weight(1f),
            value = objectId,
            onValueChange = onObjectIdChanged,
            placeholder = { Text(text = "Object ID") }
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = title,
            onValueChange = onTitleChanged,
            placeholder = { Text(text = "Title") }
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = content,
            onValueChange = onContentChanged,
            placeholder = { Text(text = "Content") }
        )*/
        Row {
            Button(onClick = {
                navController.navigate(Screen.DetailsScreen.route)

            }) {
                Text("Add")
            }
            /*Button(onClick = onUpdateClicked) {
                Text("Update")
            }*/
            /*Button(onClick = onDeleteClicked) {
                Text("Delete")
            }*/
        }
        Spacer(modifier = Modifier.height(24.dp))
        /*LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = notes, key = { it._id.toHexString() }) {
                Text(text = "${it.title}, ${it.timestamp}, ${it.userName}, ${it._id}")
            }
        }*/
        NotesList(
            notes = notes,
            onDelete = { note ->
                onDelete(note)
            },
            onDetail = { note ->
                onObjectIdChanged(note._id.toHexString())
                onTitleChanged(note.title)
                onContentChanged(note.content)
                onDetail(note)
            },
        )
    }
}
