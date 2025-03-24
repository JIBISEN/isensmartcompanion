package fr.isen.RAVAN.isensmartcompanion.dataBase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

@Parcelize
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: Date, // Maintenant, la date est stockée dans le bon type
    val category: String, // Ajout du champ category
    val location: String // Ajout du champ location
) : Parcelable

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
        id = id,
        title = title,
        description = description,
        date = parsedDate, // La date parsée est utilisée
        category = category, // Ajout du champ category
        location = location // Ajout du champ location
    )
}