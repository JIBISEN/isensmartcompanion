package fr.isen.RAVAN.isensmartcompanion

import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val location: String,
    val category: String
) : Serializable {
    fun formatDate(): String {
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        return date.format(formatter)
    }
}