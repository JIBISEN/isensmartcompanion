package fr.isen.RAVAN.isensmartcompanion

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val location: String,
    val category: String
) : Serializable {
    fun formatDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}