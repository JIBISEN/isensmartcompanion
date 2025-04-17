package fr.isen.RAVAN.isensmartcompanion.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.data.EventPreferences
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import fr.isen.RAVAN.isensmartcompanion.database.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import java.util.Date

@Composable
fun EventDetailScreen(eventId: Int, eventName: String) {
    val context = LocalContext.current
    val eventPreferences = EventPreferences(context)
    val scope = rememberCoroutineScope()
    var isNotificationEnabled by remember { mutableStateOf(false) }
    // On récupère l'état de la notification pour cet événement
    val notificationStatusFlow = eventPreferences.getEventNotificationStatus(eventId.toString())
    val notificationStatus by notificationStatusFlow.collectAsState(initial = false)

    isNotificationEnabled = notificationStatus

    Log.d("EventDetailScreen", "EventDetailScreen started")
    val db = AppDatabase.getDatabase(LocalContext.current)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Event Detail")

            // Icône de notification
            Icon(
                imageVector = if (isNotificationEnabled) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.clickable {
                    isNotificationEnabled = !isNotificationEnabled
                    scope.launch {
                        eventPreferences.saveEventNotificationStatus(eventId.toString(), isNotificationEnabled)
                        if (isNotificationEnabled) {
                            Toast.makeText(context, "Notification envoyé", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ID: $eventId")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Nom: $eventName")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                db.eventDao().insertEvent(
                    Event(
                        title = eventName,
                        date = Date(),
                        endDate = Date(),
                        isMeeting = false,
                        registered = false,
                        location = "",
                        description = "Description de $eventName"
                    )
                )
            }
        }) {
            Text(text = "Ajouter à l'agenda")
        }
    }
}