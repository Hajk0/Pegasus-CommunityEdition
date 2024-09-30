package pl.poznan.put.pegasus_communityedition.ui.navigation.dropdowns

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoreDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
    ) {
        IconButton(onClick = { onExpandedChange(true) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            DropdownMenuItem(
                text = { Text("Change Theme") },
                onClick = onThemeChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Change Theme Icon"
                    )
                }
            )
        }
    }
}
