package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.RAVAN.isensmartcompanion.database.Agenda
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import fr.isen.RAVAN.isensmartcompanion.database.Event
import fr.isen.RAVAN.isensmartcompanion.database.Inscription
import fr.isen.RAVAN.isensmartcompanion.database.RendezVous
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(db: AppDatabase, navController: NavController) {
    val userId = 0
    // Récupération des inscriptions de l'utilisateur
    val inscriptionsFlow: Flow<List<Inscription>> = db.inscriptionDao().getInscriptionsByUserId(userId)
    val inscriptions by inscriptionsFlow.collectAsState(initial = emptyList())

    // Récupération des RendezVous de l'utilisateur
    val rendezVousFlow: Flow<List<RendezVous>> = db.rendezVousDao().getRendezVousByUserId(userId)
    val rendezVousList by rendezVousFlow.collectAsState(initial = emptyList())

    // Récupération des Agendas
    val agendasFlow: Flow<List<Agenda>> = db.agendaDao().getAllAgenda()
    val agendas by agendasFlow.collectAsState(initial = emptyList())

    var showAddRendezVousForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { showAddRendezVousForm = !showAddRendezVousForm }) {
            Text(text = if (showAddRendezVousForm) "Cacher" else "Ajouter un rendez-vous")
        }
        if (showAddRendezVousForm) {
            AddRendezVousForm(db = db, userId = userId)
        }
        Spacer(modifier = Modifier.padding(16.dp))

        // Affichage des événements inscrits
        Text(text = "Mes Inscriptions")
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(inscriptions) { inscription ->
                // Récupération de l'événement correspondant à l'inscription
                val event: Event? = db.eventDao().getEventById(inscription.eventId).collectAsState(initial = null).value
                if (event != null) {
                    EventItem(event = event, navController = navController)
                }
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Affichage des éléments de l'agenda
        Text(text = "Mon Agenda")
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(agendas) { agenda ->
                AgendaItem(agenda = agenda)
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Affichage des rendez-vous
        Text(text = "Mes Rendez-Vous")
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(rendezVousList) { rendezVous ->
                RendezVousItem(rendezVous)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRendezVousForm(db: AppDatabase, userId: Int) {
    var description by remember { mutableStateOf("") }
    var startDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var endDateTime by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    var location by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = startDateTime.toString(),
            onValueChange = { /* TODO: Gérer la saisie de la date */ },
            label = { Text("Début (YYYY-MM-DDTHH:MM:SS)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = endDateTime.toString(),
            onValueChange = { /* TODO: Gérer la saisie de la date */ },
            label = { Text("Fin (YYYY-MM-DDTHH:MM:SS)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Lieu") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                db.rendezVousDao().insertRendezVous(
                    RendezVous(
                        userId = userId,
                        description = description,
                        dateDebut = startDateTime.toJavaDate(),
                        dateFin = endDateTime.toJavaDate(),
                        lieu = location
                    )
                )
            }
            description = ""
            location = ""
        }) {
            Text(text = "Ajouter le rendez-vous")
        }
    }
}

fun LocalDateTime.toJavaDate(): java.util.Date {
    return java.util.Date.from(this.atZone(java.time.ZoneId.systemDefault()).toInstant())
}

@Composable
fun RendezVousItem(rendezVous: RendezVous) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Rendez-vous: ${rendezVous.description}")
        Text(text = "Début: ${rendezVous.dateDebut}") // Ligne corrigée
        Text(text = "Fin: ${rendezVous.dateFin}") // Ligne corrigée
        Text(text = "Lieu: ${rendezVous.lieu ?: "Non spécifié"}") // ligne corrigée
    }
}