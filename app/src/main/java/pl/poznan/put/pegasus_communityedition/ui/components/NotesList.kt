package pl.poznan.put.pegasus_communityedition.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.poznan.put.pegasus_communityedition.ui.data.model.Note

@Composable
fun NotesList(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    onDetail: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes) { note ->
            NoteCard(
                title = note.title,
                content = note.content,
                online = note.online,
                onDeleteClick = { onDelete(note) },
                onDetailClick = { onDetail(note) }
            )
        }
    }
}
