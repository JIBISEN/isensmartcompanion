package fr.isen.RAVAN.isensmartcompanion.dataBase

import fr.isen.RAVAN.isensmartcompanion.Event
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class NetworkEvent(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable {
    fun toEvent(): Event {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(date, formatter)
        return Event(id, title, description, localDate, location, category)
    }
}