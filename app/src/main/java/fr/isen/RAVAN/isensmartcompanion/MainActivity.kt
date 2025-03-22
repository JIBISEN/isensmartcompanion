package fr.isen.RAVAN.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.RAVAN.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import androidx.compose.ui.graphics.vector.ImageVector
import fr.isen.RAVAN.isensmartcompanion.dataBase.NetworkEvent
import fr.isen.RAVAN.isensmartcompanion.dataBase.toEvent
import fr.isen.RAVAN.isensmartcompanion.network.RetrofitClient
import java.io.Serializable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Filled.Home)
    object Events : Screen("events", "Événements", Icons.Filled.ThumbUp)
    object Agenda : Screen("agenda", "Agenda", Icons.Filled.DateRange)
    object History : Screen("history", "Historique", Icons.Filled.List)
}

@Composable
fun MainApp() {
    Log.d("MainApp", "MainApp called")
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Navigation(navController, innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainScreen(innerPadding) }
        composable(Screen.Events.route) { EventsScreen(navController) }
        composable(Screen.Agenda.route) { AgendaScreen() }
        composable(Screen.History.route) { HistoryScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Events,
        Screen.Agenda,
        Screen.History
    )
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    val question = remember { mutableStateOf("") }
    val reponse = remember { mutableStateOf("Réponse de l'IA ici.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // ISEN Logo
            Image(
                painter = painterResource(id = R.drawable.isen),
                contentDescription = "Logo ISEN",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )
            // Title
            Text(text = "ISEN Smart Companion !")
            Spacer(modifier = Modifier.height(16.dp))

            // Text of the AI response
            Text(text = reponse.value)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
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
                        // Action lors du clic sur la flèche
                        Toast.makeText(context, "Question envoyée", Toast.LENGTH_SHORT).show()
                        reponse.value = "Vous avez demandé : ${question.value}"
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


@Composable
fun EventsScreen(navController: NavController) {
    Log.d("EventsScreen", "EventsScreen composable appelée")
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    Log.d("EventsScreen", "Début de l'appel Retrofit")
    val call = RetrofitClient.eventService.getEvents()
    call.enqueue(object : Callback<List<NetworkEvent>> {
        override fun onResponse(
            call: Call<List<NetworkEvent>>,
            response: Response<List<NetworkEvent>>
        ) {
            Log.d("EventsScreen", "onResponse appelée")
            Log.d("EventsScreen", "response.isSuccessful = ${response.isSuccessful}")
            if (response.isSuccessful) {
                Log.d("EventsScreen", "Réponse réussie")
                val networkEvents: List<NetworkEvent>? = response.body() //Ajouter le type List<NetworkEvent>?
                Log.d("EventsScreen", "response.body() = $networkEvents")
                if (networkEvents != null) {
                    events = networkEvents.map { it.toEvent() }
                    Log.d("EventsScreen", "events = $events")
                } else {
                    Log.e("EventsScreen", "response.body() est null")
                }
            } else {
                Log.e("EventsScreen", "Erreur : ${response.message()}")
                Log.e("EventsScreen", "Code d'erreur : ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<NetworkEvent>>, t: Throwable) {
            Log.e("EventsScreen", "Erreur de réseau ou problème serveur")
            Log.e("EventsScreen", "Message d'erreur: ${t.message}")
            t.printStackTrace()
        }
    })

    Log.d("EventsScreen", "Avant LazyColumn")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (events.isNotEmpty()) {
            LazyColumn {
                Log.d("EventsScreen", "LazyColumn appelée")
                items(events) { event ->
                    Log.d("EventItem", "EventItem appelée")
                    EventItem(event = event, navController = navController)
                }
            }
        } else {
            Log.d("EventsScreen", "La liste event est vide")
            Text(text = "Aucun evenement pour le moment")
        }

    }
    Log.d("EventsScreen", "Après LazyColumn")
}
@Composable
fun EventItem(event: Event, navController: NavController) {
    Log.d("EventItem", "EventItem is called : ${event.title}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(navController.context, EventDetailActivity::class.java)
                intent.putExtra(Constants.EVENT_KEY, event)
                navController.context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description)
        }
    }
}

@Composable
fun AgendaScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agenda Screen")
    }
}

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("History Screen")
    }
}