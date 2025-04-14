package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen() {
    val db = AppDatabase.getDatabase(LocalContext.current)
    val interactions = db.interactionDao().getAllInteractions().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    val interactionToDelete = remember { mutableStateOf<fr.isen.RAVAN.isensmartcompanion.database.Interaction?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Supprimer tout l'historique")
            }
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Suppression") },
                text = { Text("Voulez vous vraiment supprimer tout l'historique ?") },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            db.interactionDao().deleteAll()
                        }
                        showDialog.value = false
                    }) {
                        Text("Confirmer")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(interactions.value) { interaction ->
                InteractionItem(
                    interaction = interaction,
                    interactionToDelete = interactionToDelete,
                    coroutineScope = coroutineScope,
                    db = db
                )
            }
        }
    }
}