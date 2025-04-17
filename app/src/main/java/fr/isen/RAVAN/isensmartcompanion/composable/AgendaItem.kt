package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.database.Event

@Composable
fun AgendaItem(event: Event) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.location ?: "Pas de lieu") // Gestion de la valeur nullable
        }
    }
}