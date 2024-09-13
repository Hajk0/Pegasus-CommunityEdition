package pl.poznan.put.pegasus_communityedition.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import pl.poznan.put.pegasus_communityedition.ui.navigation.dropdowns.MoreDropdown
import pl.poznan.put.pegasus_communityedition.ui.sign_in.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    ScreenComposable: @Composable () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    // here cos do tych coskow ktore sie wysuwaja
    var expandedMore by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(title)
                },
                navigationIcon = { Icons.Filled.Home },
                actions = {
                    MoreDropdown(
                        expanded = expandedMore,
                        onExpandedChange = {
                            expandedMore = it
                        },
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScreenComposable()
        }

    }
}
