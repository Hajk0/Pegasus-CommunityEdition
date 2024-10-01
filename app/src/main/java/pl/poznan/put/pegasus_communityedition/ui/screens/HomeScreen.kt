package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) // Use theme surface color for background
            .padding(16.dp), // Padding around the screen
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically, // Aligns text and button in the center
            horizontalArrangement = Arrangement.SpaceBetween // Space between text and button
        ) {
            // "Your Notes" text in the row
            Text(
                text = "Your Notes",
                style = MaterialTheme.typography.headlineMedium, // Using theme typography
                color = MaterialTheme.colorScheme.primary
            )

            // "Add Note" button in the same row
            Button(
                onClick = { navController.navigate(Screen.DetailsScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Theme primary color
                    contentColor = MaterialTheme.colorScheme.onPrimary // Text/icon color based on primary
                ),
                shape = MaterialTheme.shapes.medium, // Use theme shape for the button
                modifier = Modifier
                    .height(48.dp) // Adjust button height for consistency
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                Text(text = "Add Note")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // List of Notes
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
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Makes the list take up the available space
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row with Insert and Update buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Spaced evenly
        ) {
            Button(onClick = onInsertClicked) {
                Text(text = "Insert")
            }

            Button(onClick = onUpdateClicked) {
                Text(text = "Update")
            }
        }
    }
}
