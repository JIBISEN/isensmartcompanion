package fr.isen.RAVAN.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Utilisation de notre composant Greeting avec la modification
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable //ui interface
fun Greeting(modifier: Modifier = Modifier) {
    val question = remember { mutableStateOf("") }
    val reponse = remember { mutableStateOf("Réponse de l'IA ici.") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo ISEN
        Image(
            painter = painterResource(id = R.drawable.isen), // Assurez-vous que le nom de votre fichier est correct
            contentDescription = "Logo ISEN",
            modifier = Modifier
                .size(100.dp) // Ajustez la taille selon vos besoins
                .padding(bottom = 16.dp)
        )
        // Titre
        Text(text = "ISEN Smart Companion")
        Spacer(modifier = Modifier.height(16.dp))

        // Champ de saisie
        TextField(
            value = question.value,
            onValueChange = { question.value = it },
            label = { Text("Posez votre question") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Bouton
        Button(onClick = {
            // Action à effectuer lors du clic sur le bouton
            reponse.value = "Traitement de la question..."
        }) {
            Text("Envoyer")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Texte de réponse de l'IA
        Text(text = reponse.value)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        Greeting()
    }
}