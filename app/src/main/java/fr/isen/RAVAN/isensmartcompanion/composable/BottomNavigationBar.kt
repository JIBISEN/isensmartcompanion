package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.RAVAN.isensmartcompanion.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(navController: NavController, scope: CoroutineScope) {
    val screens = listOf(
        Screen.Home,
        Screen.Events,
        Screen.Agenda,
        Screen.History
    )
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    androidx.compose.material3.NavigationBar(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    // Utiliser la portée fournie pour lancer la coroutine de navigation.
                    scope.launch {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}