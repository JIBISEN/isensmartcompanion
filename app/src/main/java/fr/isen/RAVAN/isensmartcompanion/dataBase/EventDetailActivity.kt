package fr.isen.RAVAN.isensmartcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.RAVAN.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import java.text.SimpleDateFormat
import java.util.Locale

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getSerializableExtra(Constants.EVENT_KEY) as? Event
        setContent {
            ISENSmartCompanionTheme {
                if (event != null) {
                    EventDetailScreen(event = event, onBack = { finish() })
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Événement non trouvé")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(event: Event, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Détail de l'événement") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = event.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date : ${event.formatDate()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Lieu : ${event.location}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Catégorie : ${event.category}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = event.description)
            Spacer(modifier = Modifier.height(16.dp))
            val context = LocalContext.current
            Button(onClick = {
                // Ici, on affiche un Toast pour indiquer que l'utilisateur est inscrit à l'événement
                Toast.makeText(context, "Vous êtes inscrit à cet événement!", Toast.LENGTH_SHORT).show()
            }) {
                Text("S'inscrire")
            }
        }
    }
}