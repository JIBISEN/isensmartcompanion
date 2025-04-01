package fr.isen.RAVAN.isensmartcompanion.composable

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.R
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import fr.isen.RAVAN.isensmartcompanion.generateContent
import fr.isen.RAVAN.isensmartcompanion.getGenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    val question = remember { mutableStateOf("") }
    val generativeModel = getGenerativeModel()
    val responseList = remember { mutableStateListOf<String>() }
    val db = AppDatabase.getDatabase(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.padding(innerPadding)) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isen),
                    contentDescription = "Logo ISEN",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )
                Text(text = "ISEN Smart Companion !")
                Spacer(modifier = Modifier.height(16.dp))
                // Affichage de l'historique des réponses
                LazyColumn {
                    items(responseList) { item ->
                        Text(text = item)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = question.value,
                    onValueChange = { question.value = it },
                    label = { Text("Posez votre question") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            // Action lorsque l'utilisateur envoie la question
                            Toast.makeText(context, "Question envoyée", Toast.LENGTH_SHORT).show()
                            responseList.add("Vous avez demandé : ${question.value}")
                            generateContent(
                                generativeModel,
                                question.value,
                            ) { result ->
                                responseList.add(result)
                                question.value = "" // Réinitialisation du champ de saisie
                                // Sauvegarde de l'interaction dans la base de données
                                coroutineScope.launch(Dispatchers.IO) {
                                    db.interactionDao().insert(
                                        fr.isen.RAVAN.isensmartcompanion.database.Interaction(
                                            question = question.value,
                                            answer = result
                                        )
                                    )
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Envoyer",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}