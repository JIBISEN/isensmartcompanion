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
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import fr.isen.RAVAN.isensmartcompanion.database.Event
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(db: AppDatabase, navController: NavController) {
    val userId = 0
    // Récupération des inscriptions de l'utilisateur
    val inscriptionsFlow: Flow<List<Event>> =
        db.eventDao().getAllEventsRegistered()
    val inscriptions by inscriptionsFlow.collectAsState(initial = emptyList())

    // Récupération des RendezVous de l'utilisateur
    val rendezVousFlow: Flow<List<Event>> = db.eventDao().getAllEventsFromMeeting()
    val rendezVousList by rendezVousFlow.collectAsState(initial = emptyList())

    var showAddRendezVousForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { showAddRendezVousForm = !showAddRendezVousForm }) {
            Text(text = if (showAddRendezVousForm) "Cacher" else "Ajouter un rendez-vous")
        }
        if (showAddRendezVousForm) {
            AddRendezVousForm(db = db, userId = userId)
        }
        Spacer(modifier = Modifier.padding(16.dp))

        // Affichage des inscriptions
        Text(text = "Mes Inscriptions")
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(inscriptions) { inscription ->
                RendezVousItem(inscription)
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

    // Gestion de la date et de l'heure de début
    val calendarStart = Calendar.getInstance()
    var startDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    val yearStart = calendarStart.get(Calendar.YEAR)
    val monthStart = calendarStart.get(Calendar.MONTH)
    val dayStart = calendarStart.get(Calendar.DAY_OF_MONTH)
    val hourStart = calendarStart.get(Calendar.HOUR_OF_DAY)
    val minuteStart = calendarStart.get(Calendar.MINUTE)

    val context = LocalContext.current

    val startDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            startDateTime = startDateTime.withYear(selectedYear).withMonth(selectedMonth + 1)
                .withDayOfMonth(selectedDayOfMonth)
            Log.d("AddRendezVousForm", "startDate : $startDateTime")
        }, yearStart, monthStart, dayStart
    )

    val startTimePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            startDateTime = startDateTime.withHour(selectedHour).withMinute(selectedMinute)
            Log.d("AddRendezVousForm", "startTime : $startDateTime")
        }, hourStart, minuteStart, true
    )

    // Gestion de la date et de l'heure de fin
    val calendarEnd = Calendar.getInstance()
    var endDateTime by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    val yearEnd = calendarEnd.get(Calendar.YEAR)
    val monthEnd = calendarEnd.get(Calendar.MONTH)
    val dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH)
    val hourEnd = calendarEnd.get(Calendar.HOUR_OF_DAY)
    val minuteEnd = calendarEnd.get(Calendar.MINUTE)

    val endDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            endDateTime = endDateTime.withYear(selectedYear).withMonth(selectedMonth + 1)
                .withDayOfMonth(selectedDayOfMonth)
            Log.d("AddRendezVousForm", "endDate : $endDateTime")

        }, yearEnd, monthEnd, dayEnd
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            endDateTime = endDateTime.withHour(selectedHour).withMinute(selectedMinute)
            Log.d("AddRendezVousForm", "endTime : $endDateTime")
        }, hourEnd, minuteEnd, true
    )

    var location by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRANCE)

    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(startInteractionSource) {
        startInteractionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                startDatePickerDialog.show()
                startTimePickerDialog.show()
            }
        }
    }

    LaunchedEffect(endInteractionSource) {
        endInteractionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                endDatePickerDialog.show()
                endTimePickerDialog.show()
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = startDateTime.format(formatter),
            onValueChange = {},
            label = { Text("Début") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            interactionSource = startInteractionSource
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = endDateTime.format(formatter),
            onValueChange = {},
            label = { Text("Fin") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            interactionSource = endInteractionSource
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
                val meeting = Event(
                    title = "Rendez-vous",
                    description = description,
                    date = startDateTime.toJavaDate(),
                    endDate = endDateTime.toJavaDate(),
                    location = location,
                    isMeeting = true,
                    registered = false
                )
                db.eventDao().insertEvent(meeting)
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
fun RendezVousItem(rendezVous: Event) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Rendez-vous: ${rendezVous.description}")
        Text(text = "Début: ${rendezVous.date}")
        Text(text = "Fin: ${rendezVous.endDate}")
        Text(text = "Lieu: ${rendezVous.location ?: "Non spécifié"}")
    }
}