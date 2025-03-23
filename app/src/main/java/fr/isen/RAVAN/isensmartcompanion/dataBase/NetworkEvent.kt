package fr.isen.RAVAN.isensmartcompanion.dataBase

import com.google.gson.annotations.SerializedName
import android.util.Log
import fr.isen.RAVAN.isensmartcompanion.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NetworkEvent(
    @SerializedName("category") val category: String,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("location") val location: String,
    @SerializedName("title") val title: String
)

fun NetworkEvent.toEvent(): Event {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormat.parse(this.date) ?: Date() // Utiliser une date par défaut si la conversion échoue
    return Event(
        id = this.id,
        title = this.title,
        description = this.description,
        date = date,
        location = this.location,
        category = this.category
    )
}