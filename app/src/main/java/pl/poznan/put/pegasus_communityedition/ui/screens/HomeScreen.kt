package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import pl.poznan.put.pegasus_communityedition.Screen
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
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
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
        )
        Button(onClick = onInsertClicked) {
            Text("Add")
        }
        Button(onClick = onUpdateClicked) {
            Text("Update")
        }
        Button(onClick = onDeleteClicked) {
            Text("Delete")
        }
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = notes, key = { it._id.toHexString() }) {
                Text(text = "${it.title}, ${it.timestamp}, ${it.userName}, ${it._id}")
            }
        }
    }
}
