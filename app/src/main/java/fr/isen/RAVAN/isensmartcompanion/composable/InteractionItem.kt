package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun InteractionItem(
    interaction: fr.isen.RAVAN.isensmartcompanion.database.Interaction,
    interactionToDelete: MutableState<fr.isen.RAVAN.isensmartcompanion.database.Interaction?>,
    coroutineScope: CoroutineScope, db: AppDatabase
){
    var showDialogDeleteOneItem by remember { mutableStateOf(false) }
    // Conversion de LocalDateTime en Date
    val date: Date = Date()
    val format = java.text.SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.FRENCH)
    val interactionDate = format.format(date) // On formate la date pour l'afficher correctement
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = interactionDate)
                IconButton(onClick = {
                    showDialogDeleteOneItem = true
                    interactionToDelete.value = interaction
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                }
            }
            if(showDialogDeleteOneItem && interactionToDelete.value != null){
                AlertDialog(
                    onDismissRequest = { showDialogDeleteOneItem = false },
                    title = { Text("Suppression") },
                    text = { Text("Voulez vous vraiment supprimer cette interaction ?") },
                    confirmButton = {
                        Button(onClick = {
                            coroutineScope.launch(Dispatchers.IO){
                                db.interactionDao().delete(interaction)
                            }
                            interactionToDelete.value = null
                            showDialogDeleteOneItem = false
                        }) {
                            Text("Confirmer")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            interactionToDelete.value = null
                            showDialogDeleteOneItem = false
                        }) {
                            Text("Annuler")
                        }
                    }
                )
            }
            Text(text = "Question : ${interaction.question}")
            Text(text = "Réponse : ${interaction.answer}")
        }
    }
}
