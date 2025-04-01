package fr.isen.RAVAN.isensmartcompanion.composable

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//import fr.isen.RAVAN.isensmartcompanion.EventItem
import fr.isen.RAVAN.isensmartcompanion.database.Event
import fr.isen.RAVAN.isensmartcompanion.database.NetworkEvent
import fr.isen.RAVAN.isensmartcompanion.database.toEvent
import fr.isen.RAVAN.isensmartcompanion.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventsScreen(navController: NavController) {
    Log.d("EventsScreen", "EventsScreen composable appelée")
    var events by remember { mutableStateOf<List<Event>?>(null) }
    var error by remember { mutableStateOf<Boolean>(false) }
    var isLoading by remember { mutableStateOf<Boolean>(true) }

    Log.d("EventsScreen", "Début de l'appel Retrofit")
    val call = RetrofitClient.eventService.getEvents()
    call.enqueue(object : Callback<List<NetworkEvent>> {
        override fun onResponse(
            call: Call<List<NetworkEvent>>,
            response: Response<List<NetworkEvent>>
        ) {
            Log.d("EventsScreen", "onResponse appelée")
            Log.d("EventsScreen", "response.isSuccessful = ${response.isSuccessful}")
            isLoading = false
            if (response.isSuccessful) {
                Log.d("EventsScreen", "Réponse réussie")
                val networkEvents: List<NetworkEvent>? = response.body() //Ajouter le type List<NetworkEvent>?
                Log.d("EventsScreen", "response.body() = $networkEvents")
                if (networkEvents != null) {
                    events = networkEvents.map { it.toEvent()}
                    Log.d("EventsScreen", "events = $events")
                    error = false
                } else {
                    Log.e("EventsScreen", "response.body() est null")
                    error = true
                }
            } else {
                Log.e("EventsScreen", "Erreur : ${response.message()}")
                Log.e("EventsScreen", "Code d'erreur : ${response.code()}")
                error = true
            }
        }

        override fun onFailure(call: Call<List<NetworkEvent>>, t: Throwable) {
            Log.e("EventsScreen", "Erreur de réseau ou problème serveur")
            Log.e("EventsScreen", "Message d'erreur: ${t.message}")
            t.printStackTrace()
            isLoading = false
            error = true
        }
    })

    Log.d("EventsScreen", "Avant LazyColumn")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                Log.d("EventsScreen", "isLoading")
                Text(text = "Chargement...")
            }

            error -> {
                Log.d("EventsScreen", "error")
                Text(text = "Une erreur est survenue")
            }

            events != null -> {
                Log.d("EventsScreen", "events != null")
                if (events!!.isNotEmpty()) {
                    LazyColumn {
                        Log.d("EventsScreen", "LazyColumn appelée")
                        items(events ?: emptyList()) { event ->
                            Log.d("EventItem", "EventItem appelée")
                            EventItem(event = event, navController = navController)
                        }
                    }
                } else {
                    Log.d("EventsScreen", "La liste event est vide")
                    Text(text = "Aucun événement pour le moment")
                }
            }

            else -> {
                Log.d("EventsScreen", "else")
                Text(text = "Une erreur inatendue est survenue")
            }
        }
    }
    Log.d("EventsScreen", "Après LazyColumn")
}