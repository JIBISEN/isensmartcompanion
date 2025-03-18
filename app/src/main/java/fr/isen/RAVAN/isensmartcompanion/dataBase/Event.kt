package fr.isen.RAVAN.isensmartcompanion.dataBase

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
): Serializable {
    fun formatDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatter)
    }
}