package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mongodb.kbson.ObjectId
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note

@Composable
fun DetailsScreen(
    note: Note?,
    title: String,
    content: String,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: (Note) -> Unit,
) {

    // Create mutable states for title and content fields
    // var title by remember { mutableStateOf(note?.title ?: "") }
    // var content by remember { mutableStateOf(note?.content ?: "") }

    // Main content layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Padding around the screen
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Title Label
        Text(
            text = "Title",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Editable Title TextField
        TextField(
            value = title,
            onValueChange = onTitleChanged,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Content Label
        Text(
            text = "Content",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Editable Content TextField
        TextField(
            value = content,
            onValueChange = onContentChanged,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons Row (Save and Delete)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Delete Button
            if (note != null) {
                Button(
                    onClick = { onDeleteClicked(note) }, // TODO( check this )
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Delete")
                }
            } else {
                Button(
                    onClick = {
                        onDeleteClicked(Note())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Cancel")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Save Button
            Button(
                onClick = onSaveClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Information (optional)
        if (note != null) {
            Text(
                text = "Created by: ${note.userName}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}