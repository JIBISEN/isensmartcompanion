package fr.isen.RAVAN.isensmartcompanion.composable

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.RAVAN.isensmartcompanion.R
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import fr.isen.RAVAN.isensmartcompanion.database.Interaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import fr.isen.RAVAN.isensmartcompanion.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context,
    generateContent: suspend (
        coroutineScope: CoroutineScope,
        generativeModel: GenerativeModel,
        userInput: String,
        onResult: (String) -> Unit
    ) -> Unit
) {
    MainScreen(
        innerPadding = PaddingValues(0.dp),
        generateContent = generateContent,
        getGenerativeModel = { (context as MainActivity).getGenerativeModel() },
        context = context,
        coroutineScope = rememberCoroutineScope(),
        responseList = remember { mutableStateListOf<String>() },
        db = AppDatabase.getDatabase(context)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    innerPadding: PaddingValues,
    generateContent: suspend (
        coroutineScope: CoroutineScope,
        generativeModel: GenerativeModel,
        userInput: String,
        onResult: (String) -> Unit
    ) -> Unit,
    getGenerativeModel: () -> GenerativeModel,
    context: Context,
    coroutineScope: CoroutineScope,
    responseList: MutableList<String>,
    db: AppDatabase
) {
    val question = remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.padding(innerPadding)) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Partie du haut
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Logo ISEN
                Image(
                    painter = painterResource(id = R.drawable.isen),
                    contentDescription = "Logo ISEN",
                    modifier = Modifier
                        .padding(16.dp)
                        .height(50.dp)
                        .width(200.dp)
                )

                Text(text = "ISEN Smart Companion !", modifier = Modifier.padding(16.dp))

                // Espace entre le logo et la zone de contenu
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Zone de contenu (scrollable)
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // La zone de contenu prend tout l'espace disponible en hauteur
                    .fillMaxWidth() // La zone de contenu prend toute la largeur disponible
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(responseList.reversed()) { message ->
                    Text(text = message, modifier = Modifier.padding(8.dp))
                }
            }

            // Partie du bas
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Barre de saisie
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = question.value,
                        onValueChange = { question.value = it },
                        label = { Text("Posez votre question") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray,
                            unfocusedContainerColor = Color.LightGray,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                Toast.makeText(context, "Question envoyée", Toast.LENGTH_SHORT).show()
                                responseList.add("Vous avez demandé : ${question.value}")
                                coroutineScope.launch {
                                    generateContent(
                                        coroutineScope,
                                        getGenerativeModel(),
                                        question.value,
                                    ) { result ->
                                        responseList.add(result)
                                        question.value = ""
                                        coroutineScope.launch(Dispatchers.IO) {
                                            db.interactionDao().insert(
                                                Interaction(
                                                    question = question.value,
                                                    answer = result
                                                )
                                            )
                                        }
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
}