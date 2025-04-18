package fr.isen.RAVAN.isensmartcompanion.database

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

data class NetworkEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: String, // La date est reçue en string depuis le réseau
    val category: String, // Ajout du champ category
    val location: String // Ajout du champ location
)

fun NetworkEvent.toEvent(): Event {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH) // Format de date et Locale français
    val parsedDate = try {
        format.parse(date) ?: Date() // On essaie de parser la date, si c'est null on donne une date de base
    } catch (e: Exception){
        Date() // On donne une date de base si l'analyse echoue
    }
    return Event(
        eventId = id,
        title = title,
        description = description,
        date = parsedDate, // La date parsée est utilisée
        category = category, // Ajout du champ category
        endDate = parsedDate,
        isMeeting = false,
        registered = false,
        location = location // Ajout du champ location
    )
}