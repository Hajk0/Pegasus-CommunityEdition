package pl.poznan.put.pegasus_communityedition.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
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
            color = MaterialTheme.colorScheme.onSurfaceVariant // Use theme color
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Editable Title TextField
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            TextField(
                value = title,
                onValueChange = onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Background color for the text field
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Content Label
        Text(
            text = "Content",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Use theme color
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Editable Content TextField
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            TextField(
                value = content,
                onValueChange = onContentChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Fixed height for the content field
                textStyle = MaterialTheme.typography.bodyLarge,
                maxLines = 10, // Limit max lines for content
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Background color for the text field
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons Row (Save and Delete)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Space between buttons
        ) {
            // Delete or Cancel Button (with icons)
            if (note != null) {
                Button(
                    onClick = { onDeleteClicked(note) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer, // Error color for delete
                        contentColor = MaterialTheme.colorScheme.onErrorContainer // Text/icon color for error button
                    ),
                    modifier = Modifier.weight(1f) // Make button take up available width
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Delete icon
                        contentDescription = "Delete Note",
                        modifier = Modifier.size(20.dp) // Icon size
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Delete")
                }
            } else {
                Button(
                    onClick = {
                        onDeleteClicked(Note()) // Trigger onDelete for canceling
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer, // Secondary color for cancel
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer // Text/icon color for cancel
                    ),
                    modifier = Modifier.weight(1f) // Make button take up available width
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel, // Cancel icon
                        contentDescription = "Cancel",
                        modifier = Modifier.size(20.dp) // Icon size
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cancel")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Save Button (with icon)
            Button(
                onClick = onSaveClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Primary color for save
                    contentColor = MaterialTheme.colorScheme.onPrimary // Text/icon color for save button
                ),
                modifier = Modifier.weight(1f) // Make button take up available width
            ) {
                Icon(
                    imageVector = Icons.Default.Save, // Save icon
                    contentDescription = "Save Note",
                    modifier = Modifier.size(20.dp) // Icon size
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Save")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Information (optional)
        if (note != null) {
            Text(
                text = "Created by: ${note.userName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}