package fr.isen.RAVAN.isensmartcompanion.composable

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.RAVAN.isensmartcompanion.Constants
import fr.isen.RAVAN.isensmartcompanion.EventDetailActivity
import fr.isen.RAVAN.isensmartcompanion.database.Event
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventItem(event: Event, navController: NavController) {
    Log.d("EventItem", "EventItem is called : ${event.title}")
    val format = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
    val eventDate = format.format(event.date) // On formate la date pour l'afficher correctement
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Création de l'intent pour démarrer l'activité EventDetailActivity
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra(Constants.EVENT_KEY, event)
                }
                // Démarrage de l'activité
                context.startActivity(intent)
            }
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = eventDate)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.category)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.location)
        }
    }
}